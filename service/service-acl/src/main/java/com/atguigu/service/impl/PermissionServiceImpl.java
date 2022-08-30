package com.atguigu.service.impl;

import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Permission;
import com.atguigu.helper.PermissionHelper;
import com.atguigu.mapper.PermissionMapper;
import com.atguigu.mapper.RolePermissionMapper;
import com.atguigu.service.PermissionService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {
    
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    
    @Override
    public BaseMapper<Permission> getBaseMapper() {
        return permissionMapper;
    }

    /**
     * 根据roleId获取该角色所有权限数据
     * @param roleId
     * @return
     */
    @Override
    public List<Map<String, Object>> findZNodesByRoleId(Long roleId) {
        //已分配的权限,未分配的权限
        ArrayList<Map<String, Object>> zNodes = new ArrayList<>();
        //查询所有权限
        List<Permission> allPermissionlist = permissionMapper.findAll();
        //查询已分配的权限id
        List<Long> assignPermissionIdList = rolePermissionMapper.findPermissionIdListByRoleId(roleId);
        
        for (Permission permission : allPermissionlist) {
            HashMap<String, Object> zNode = new HashMap<>();
            if (assignPermissionIdList.contains(permission.getId())){
                //已分配的权限
                zNode.put("checked", true);
            }else {
                //未分配的权限
                zNode.put("checked", false);
            }
            zNode.put("id", permission.getId());
            zNode.put("pId", permission.getParentId());
            zNode.put("name", permission.getName());
            zNode.put("open", true);
            zNodes.add(zNode);
        }
        return zNodes;
    }

    /**
     * 根据adminId查询菜单信息
     * @param adminId
     * @return
     */
    @Override
    public List<Permission> findMenusByAdminId(Long adminId) {
        List<Permission> permissionList = permissionMapper.findMenusByAdminId(adminId);
        return PermissionHelper.build(permissionList);
    }

    /**
     * 查询所有菜单信息
     * @return
     */
    @Override
    public List<Permission> findAllMenus() {
        List<Permission> permissionList = permissionMapper.findAll();
        return PermissionHelper.build(permissionList);
    }

    /**
     * 根据用户id查询权限信息
     * @param adminId
     * @return
     */
    @Override
    public List<Permission> findListByAdminId(Long adminId) {
        return permissionMapper. findListByAdminId(adminId);
    }
}
