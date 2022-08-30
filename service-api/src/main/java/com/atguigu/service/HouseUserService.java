package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseUser;

import java.util.List;

public interface HouseUserService extends BaseService<HouseUser> {
    /**
     * 根据房源id获取房东信息
     * @param houseId
     * @return
     */
    List<HouseUser> findListByHouseId(Long houseId);
}
