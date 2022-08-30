package com.atguigu.base;

import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {
    /**
     * 获取所有角色
     * @return
     */
    List<T> findAll();

    /**
     * 添加角色
     * @param role
     */
    int insert(T role);

    /**
     * 根据id获取角色
     * @param id
     * @return
     */
    T getById(Long id);

    /**
     * 修改角色
     * @param role
     * @return
     */
    int update(T role);

    /**
     *删除用户
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 分页条件查询
     * @param filters roleName
     * @return
     */
    List<T> findPage(Map filters);
}

