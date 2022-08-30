package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/userFollow")
public class UserFollowController extends BaseController {
    
    @Reference
    private UserFollowService userFollowService;

    /**
     * 添加关注
     * @param id : 房源id
     * @param session : 会话域
     * @return
     */
    @ResponseBody
    @RequestMapping("/auth/follow/{id}")
    public Result addFollow(@PathVariable Long id, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        Long userId = userInfo.getId();
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(userId);
        userFollow.setHouseId(id);
        userFollowService.insert(userFollow);
        return Result.ok();
    }

    /**
     * 取消关注
     * @param id : 房源id
     * @param session : 会话域
     * @return
     */
    @ResponseBody
    @RequestMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable Long id, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        Long userId = userInfo.getId();
        userFollowService.deleteByUserIdAndHouseId(userId, id);
        return Result.ok();
    }

    /**
     * 分页查询关注列表
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("/auth/list/{pageNum}/{pageSize}")
    public Result findListPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        PageInfo<UserFollowVo> pageInfo = userFollowService.findByPage(pageNum, pageSize, userInfo.getId());
        return Result.ok(pageInfo);
    }

    /**
     * 取消关注
     * @param id : user_follow的主键id
     * @return
     */
    @ResponseBody
    @RequestMapping("/auth/cancelFollowById/{id}")
    public Result cancelFollowById(@PathVariable Long id){
        userFollowService.cancelFollowByKey(id);
        return Result.ok();
    }
    
}
