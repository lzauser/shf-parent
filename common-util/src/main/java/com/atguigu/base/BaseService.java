package com.atguigu.base;

import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface BaseService<T> {
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
     * 删除角色
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 分页查询
     * @param filters : 搜索条件(roleName, pageNum, pageSize)
     * @return
     */
    PageInfo<T> findPage(Map filters);
}
