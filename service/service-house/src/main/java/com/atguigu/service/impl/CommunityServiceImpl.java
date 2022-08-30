package com.atguigu.service.impl;

import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Community;
import com.atguigu.mapper.CommunityMapper;
import com.atguigu.mapper.DictMapper;
import com.atguigu.service.CommunityService;
import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {

    @Autowired
    private CommunityMapper communityMapper;

    @Autowired
    private DictMapper dictMapper;

    @Override
    public BaseMapper<Community> getBaseMapper() {
        return communityMapper;
    }

    @Override
    public PageInfo<Community> findPage(Map filters) {
        //当前页数
        Integer pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        //每页显示的记录数
        Integer pageSize = CastUtil.castInt(filters.get("pageSize"), 5);
        //执行分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Community> page = communityMapper.findPage(filters);
        for (Community community : page) {
            //根据areaId获取areaName
            String areaName = dictMapper.getNameById(community.getAreaId());
            community.setAreaName(areaName);
            //根据plateId获取plateName
            String plateName = dictMapper.getNameById(community.getPlateId());
            community.setPlateName(plateName);
        }
        return new PageInfo<>(page);
    }
}
