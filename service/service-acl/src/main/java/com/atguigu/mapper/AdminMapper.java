package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Admin;

import java.util.List;

public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 根据用户名获取用户信息
     * @param username : 用户名
     * @return
     */
    Admin getByUsername(String username);
}
