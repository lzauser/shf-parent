package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 根据用户id查询权限列表
     * @param adminId
     * @return
     */
    List<Permission> findListByAdminId(Long adminId);

    /**
     * 根据adminId查询菜单信息
     * @param adminId
     * @return
     */
    List<Permission> findMenusByAdminId(Long adminId);
}
