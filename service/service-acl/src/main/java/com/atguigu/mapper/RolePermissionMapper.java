package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.RolePermission;

import java.util.List;

public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    List<Long> findPermissionIdListByRoleId(Long roleId);

    /**
     * 删除之前的权限
     * @param roleId
     */
    void deleteByRoleId(Long roleId);
}
