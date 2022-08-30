package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.bo.LoginBo;
import com.atguigu.entity.bo.RegisterBo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/userInfo")
public class UserInfoController extends BaseController {

    private static final String LIST_ACTION = "redirect:/index.html";
    @Reference
    private UserInfoService userInfoService;
    @Autowired
    private JedisPool jedisPool;
    
    /**
     * 发送验证码(注册)
     * @param phone
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable String phone){
        //发送验证码(阿里云)
        String existCode = "6666";
        //session.setAttribute("existCode", existCode);
        Jedis jedis = jedisPool.getResource();
        jedis.setex("existCode", 10, existCode);
        jedis.close();
        return Result.ok();
    }

    /**
     * 用户注册
     * @param registerBo : 封装注册信息
     * @param session : 会话域
     * @return
     */
    @RequestMapping("register")
    public Result register(@RequestBody RegisterBo registerBo, HttpSession session){
        //1.校验验证码
        //获取输入的验证码
        String inputCode = registerBo.getCode();
        //获取生成的验证码
        //String existCode = (String) session.getAttribute("existCode");
        Jedis jedis = jedisPool.getResource();
        String existCode = jedis.get("existCode");
        if (existCode == null){
            //验证码过期
            jedis.close();
            return Result.build(null, ResultCodeEnum.CODE_PAST_DUE);
        }
        if (!inputCode.equals(existCode)){
            //验证码错误
            jedis.close();
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }
        
        //2.验证手机号是否可用
        //通过手机号查用户信息
        UserInfo userInfo = userInfoService.getByPhone(registerBo.getPhone());
        //存在此用户,手机号不可用
        if(userInfo != null){
            jedis.close();
            return Result.build(null, ResultCodeEnum.PHONE_REGISTER_ERROR);
        }
        
        //以上通过则注册用户
        //3.注册用户
        userInfo = new UserInfo();
        userInfo.setPhone(registerBo.getPhone());
        userInfo.setPassword(registerBo.getPassword());
        userInfo.setNickName(registerBo.getNickName());
        userInfo.setStatus(1);
        userInfoService.insert(userInfo);
        jedis.close();
        return Result.ok();
    }

    /**
     * 用户登录
     * @param loginBo : 封装用户登录信息
     * @param session : 绘画域
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    public Result login(@RequestBody LoginBo loginBo, HttpSession session) {
        //1. 判断手机号是否正确
        UserInfo userInfo = userInfoService.getByPhone(loginBo.getPhone());
        if (null == userInfo) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
        }
        //2. 判断密码是否正确
        if (!userInfo.getPassword().equals(loginBo.getPassword())) {
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
        }
        //3. 判断账户是否可用
        if (0 == userInfo.getStatus()) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }
        //4. 登录成功，将用户信息保存到session
        session.setAttribute("userInfo", userInfo);
        //5. 将用户信息封装到Result对象
        return Result.ok(userInfo);
    }

    /**
     * 用户注销
     * @param session : 会话域
     * @return
     */
    @ResponseBody
    @RequestMapping("/logout")
    public Result logout(HttpSession session){
        session.removeAttribute("userInfo");
        return Result.ok();
    }
}
