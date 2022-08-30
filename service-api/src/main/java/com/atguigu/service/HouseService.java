package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.House;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.github.pagehelper.PageInfo;

public interface HouseService extends BaseService<House> {
    /**
     * 发布房源
     * @param houseId
     * @param status
     */
    void publish(Long houseId, Integer status);

    /**
     * 分页查询
     * @param pageNum : 当前页数
     * @param pageSize : 每页记录数
     * @param houseQueryBo : 搜索条件， 排序条件
     * @return
     */
    PageInfo<HouseVo> findByPage(Integer pageNum, Integer pageSize, HouseQueryBo houseQueryBo);
    
}
