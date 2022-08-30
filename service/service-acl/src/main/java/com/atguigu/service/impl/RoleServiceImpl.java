package com.atguigu.service.impl;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.RolePermission;
import com.atguigu.mapper.AdminMapper;
import com.atguigu.mapper.AdminRoleMapper;
import com.atguigu.mapper.RoleMapper;
import com.atguigu.entity.Role;
import com.atguigu.mapper.RolePermissionMapper;
import com.atguigu.service.RoleService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public BaseMapper<Role> getBaseMapper() {
        return roleMapper;
    }

    /**
     * 根据adminId获取角色信息
     * @param adminId
     * @return
     */
    @Override
    public Map<String, Object> findRoleListByAdminId(Long adminId) {
        HashMap<String, Object> map = new HashMap<>();
        //roleList : 所有角色
        List<Role> allRoleList = roleMapper.findAll();
        //assignRoleIdList : 已分配角色的id
        List<Long> assignRoleIdList =  adminRoleMapper.findRoleIdListByAdminId(adminId);
        ArrayList<Role> assignRoleList = new ArrayList<>();
        ArrayList<Role> unAssignRoleList = new ArrayList<>();
        for (Role role : allRoleList) {
            if (assignRoleIdList.contains(role.getId())){
                //assignRoleList : 已分配角色列表
                assignRoleList.add(role);
            }else {
                //unAssignRoleList : 未分配角色列表
                unAssignRoleList.add(role);
            }
        }
        map.put("assignRoleList", assignRoleList);
        map.put("unAssignRoleList", unAssignRoleList);
        return map;
        
    }

    /**
     * 给角色分配权限
     * @param roleId : 角色id
     * @param permissionIds : 一个角色可以分配多个权限
     * @return
     */
    @Override
    public void assignPermission(Long roleId, Long[] permissionIds) {
        //删除之前的权限
        rolePermissionMapper.deleteByRoleId(roleId);
        //添加新的权限
        for (Long permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionMapper.insert(rolePermission);
        }
    }
}