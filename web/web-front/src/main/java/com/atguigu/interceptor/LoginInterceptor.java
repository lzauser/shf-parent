package com.atguigu.interceptor;

import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.util.WebUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
        //判断是否登录
        if (userInfo == null){
            //未登录
            WebUtil.writeJSON(response, Result.build("未登录", ResultCodeEnum.LOGIN_AUTH));
            return false;
        }
        //已登录
        return true;
    }
}
