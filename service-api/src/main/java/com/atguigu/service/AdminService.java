package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Admin;

public interface AdminService extends BaseService<Admin> {
    /**
     * 分配角色
     * @param adminId
     * @param roleIds
     */
    void assignRole(Long adminId, Long[] roleIds);

    /**
     * 根据username查询用户信息
     * @param username
     * @return
     */
    Admin getByUsername(String username); 
}
