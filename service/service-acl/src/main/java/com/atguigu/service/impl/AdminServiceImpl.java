package com.atguigu.service.impl;

import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Admin;
import com.atguigu.entity.AdminRole;
import com.atguigu.mapper.AdminMapper;
import com.atguigu.mapper.AdminRoleMapper;
import com.atguigu.service.AdminService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Override
    public BaseMapper<Admin> getBaseMapper() {
        return adminMapper;
    }

    /**
     * 保存角色的分配
     * @param adminId : 用户id
     * @param roleIds : 角色id数组(一个用户可以有多个角色)
     */
    @Override
    public void assignRole(Long adminId, Long[] roleIds) {
        //删除之前已经分配的角色
        adminRoleMapper.deleteByAdminId(adminId);
        //添加新的分配的角色
        for (Long roleId : roleIds) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            adminRoleMapper.insert(adminRole);
        }
    }

    @Override
    public Admin getByUsername(String username) {
        return adminMapper.getByUsername(username);
    }
}
