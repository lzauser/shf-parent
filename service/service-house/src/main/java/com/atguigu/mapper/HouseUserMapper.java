package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseUser;

import java.util.List;

public interface HouseUserMapper extends BaseMapper<HouseUser> {
    /**
     * 根据房源id获取房东信息
     * @param houseId : 房源id
     * @return
     */
    List<HouseUser> findListByHouseId(Long houseId);
}
