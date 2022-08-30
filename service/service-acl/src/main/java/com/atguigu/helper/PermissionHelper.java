package com.atguigu.helper;

import com.atguigu.entity.Permission;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {
    public static List<Permission> build(List<Permission> originalPermissionList){
        List<Permission> treeNodes = new ArrayList<>();
        for (Permission treeNode : originalPermissionList) {
            if(null!=treeNode){
                if (treeNode.getParentId() == 0) {
                    //一级菜单
                    treeNode.setLevel(1);
                    treeNode.setChildren(getChildren(treeNode,originalPermissionList));
                    treeNodes.add(treeNode);
                }
            }

        }

        return treeNodes;
    }

    /**
     * 获取子菜单列表
     * @return
     */
    private static List<Permission> getChildren(Permission treeNode, List<Permission> originalPermissionList) {

        List<Permission> children = new ArrayList<>();
        for (Permission childTreeNode : originalPermissionList) {
            if(null!=childTreeNode){
                //找出子节点
                if (treeNode.getId().longValue() == childTreeNode.getParentId().longValue()) {
                    //设置子节菜单级别
                    childTreeNode.setLevel(treeNode.getLevel() + 1);
                    //设置子菜单的父菜单名
                    childTreeNode.setParentName(treeNode.getName());
                    //给子菜单设置子菜单
                    childTreeNode.setChildren(getChildren(childTreeNode,originalPermissionList));

                    children.add(childTreeNode);
                }
            }

        }
        return children;
    }
}