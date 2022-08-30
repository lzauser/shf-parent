package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.AdminRole;

import java.util.List;

public interface AdminRoleMapper extends BaseMapper<AdminRole> {
    /**
     * 根据adminId获取角色信息
     * @param adminId
     * @return
     */
    List<Long> findRoleIdListByAdminId(Long adminId);

    /**
     * 删除之前已经分配的角色
     * @param adminId : 用户id
     */
    void deleteByAdminId(Long adminId);
}
