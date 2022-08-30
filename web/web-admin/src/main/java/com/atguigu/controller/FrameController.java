package com.atguigu.controller;

import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class FrameController {

    private static final String PAGE_INDEX = "frame/index";
    private static final String PAGE_MAIN = "frame/main";
    private static final String PAGE_LOGIN = "frame/login";
    private static final String PAGE_AUTH = "frame/auth";

    @Reference
    private AdminService adminService;
    @Reference
    private PermissionService permissionService;
    
    /**
     * 跳转frame/index.html页面
     * @return
     */
    @RequestMapping("/")
    public String index(Model model){
        //从security容器中获取登录的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Admin admin = adminService.getByUsername(username);
        Long adminId = admin.getId();
        //admin
        model.addAttribute("admin", admin);
        //permissionList
        List<Permission> permissionList = permissionService.findMenusByAdminId(adminId);
        model.addAttribute("permissionList", permissionList);
        return PAGE_INDEX;
    }

    /**
     * 框架主页
     * @return
     */
    @RequestMapping("/main")
    public String main(){
        return PAGE_MAIN;
    }

    /**
     * 跳转'frame/login.html'页面
     *
     * @return
     */
    @RequestMapping("/login")
    public String login() {
        return PAGE_LOGIN;
    }

    @RequestMapping("/auth")
    public String auth(){
        return PAGE_AUTH;
    }
    
}
