package com.atguigu.log;


import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import org.apache.dubbo.config.annotation.Reference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@Aspect
public class ControllerAspect {

    @Reference
    private AdminService adminService;

    Logger logger = LoggerFactory.getLogger(ControllerAspect.class);


    @Around("execution(* *..*Controller.*(..))")
    public Object controllerAround(ProceedingJoinPoint pjp) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            //没有登录 , 直接放行。
            return pjp.proceed();
        }
        String name = authentication.getName();
        if (null == name || "anonymousUser".equals(name)) {
            //没有登录，直接放行
            return pjp.proceed();
        }

        Long startTime = System.currentTimeMillis();

        //用户信息
        String userName = authentication.getName();
        Admin admin = adminService.getByUsername(userName);
        Long userId = admin.getId();

        //请求信息
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String ip = request.getRemoteAddr();

        //方法信息
        Signature signature = pjp.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();

        StringBuilder params = new StringBuilder();
        Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (i < args.length - 1) {
                //不是最后一个参数 ， 有逗号
                params.append("param" + (i + 1) + ":" + arg + ",");
            } else {
                //是最后一个参数，没有逗号
                params.append("param" + (i + 1) + ":" + arg);
            }
        }


        //执行切入点
        Object result = pjp.proceed();
        Long endTime = new Date().getTime();

        Long time = endTime - startTime;


        String log =
                "用户编号 : " +
                        userId + " -- 用户名 : " +
                        userName + " -- 请求路径 : " +
                        url + " -- 请求方式 : " +
                        method + " -- ip地址 : " +
                        ip + " -- 类名 : " +
                        className + " -- 方法名 : " +
                        methodName + " -- 参数 : " +
                        (params.toString().equals("") ? "无参数" : params) + " -- 运行时长 : " +
                        time + "毫秒";
        logger.debug(log);

        return result;
    }

}
