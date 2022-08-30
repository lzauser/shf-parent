package com.atguigu.base;

import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl<T> {

    /**
     * 获取BaseMapper
     * @return
     */
    public abstract BaseMapper<T> getBaseMapper();

    public List<T> findAll() {
        return getBaseMapper().findAll();
    }

    public int insert(T role) {
        return getBaseMapper().insert(role);
    }

    public T getById(Long id) {
        return getBaseMapper().getById(id);
    }

    public int update(T role) {
        return getBaseMapper().update(role);
    }

    public int delete(Long id) {
        return getBaseMapper().delete(id);
    }

    public PageInfo<T> findPage(Map filters) {
        Integer pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        Integer pageSize = CastUtil.castInt(filters.get("pageSize"), 5);
        //执行分页查询
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<T>(getBaseMapper().findPage(filters));
    }
}
