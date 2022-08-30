package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseBroker;

import java.util.List;

public interface HouseBrokerService extends BaseService<HouseBroker> {
    /**
     * 根据房源id获取经纪人信息
     * @param houseId : 房源id
     * @return
     */
    List<HouseBroker> findListByHouseId(Long houseId);
}
