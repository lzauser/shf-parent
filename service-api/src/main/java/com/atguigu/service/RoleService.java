package com.atguigu.service;
import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;

import java.util.Map;

public interface RoleService extends BaseService<Role> {

    /**
     * 根据adminId获取角色信息
     * @param adminId
     * @return
     */
    Map<String, Object> findRoleListByAdminId(Long adminId);

    /**
     * 给角色分配权限
     * @param roleId : 角色id
     * @param permissionIds: 一个角色可以分配多个权限
     */
    void assignPermission(Long roleId, Long[] permissionIds);
}