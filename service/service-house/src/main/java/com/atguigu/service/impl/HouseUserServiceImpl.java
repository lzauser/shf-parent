package com.atguigu.service.impl;

import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.HouseUser;
import com.atguigu.mapper.HouseUserMapper;
import com.atguigu.service.HouseUserService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class HouseUserServiceImpl extends BaseServiceImpl<HouseUser> implements HouseUserService {

    @Autowired
    private HouseUserMapper houseUserMapper;

    @Override
    public BaseMapper<HouseUser> getBaseMapper() {
        return houseUserMapper;
    }

    @Override
    public List<HouseUser> findListByHouseId(Long houseId) {
        return houseUserMapper.findListByHouseId(houseId);
    }
}
