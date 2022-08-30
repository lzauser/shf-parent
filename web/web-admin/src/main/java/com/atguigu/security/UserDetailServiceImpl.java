package com.atguigu.security;

import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Reference
    private AdminService adminService;

    @Reference
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据username查询用户信息
        Admin admin = adminService.getByUsername(username);
        if (null == admin) {
            //账户错误 , 登录失败
            return null;
        }
        //账户正确 , 设置权限列表 (权限code)
        List<Permission> permissionList = permissionService.findListByAdminId(admin.getId());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Permission permission : permissionList) {
            if (null == permission.getCode() || "".equals(permission.getCode())) {
                //不要将code为null的数据存储到authorities中
                continue;
            }
            authorities.add(new SimpleGrantedAuthority(permission.getCode()));
        }
        return new User(admin.getUsername(), admin.getPassword(), authorities);
    }
}
