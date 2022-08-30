package com.atguigu.service.impl;

import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.House;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.mapper.HouseMapper;
import com.atguigu.service.HouseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {

    @Autowired
    private HouseMapper houseMapper;

    @Override
    public BaseMapper<House> getBaseMapper() {
        return houseMapper;
    }

    /**
     * 发布房源
     * @param id : 房源id
     * @param status 房源发布状态(未发布: 0 / 已发布: 1 )
     */
    @Override
    public void publish(Long id, Integer status) {
        House house = new House();
        house.setId(id);
        house.setStatus(status);
        houseMapper.update(house);
    }

    /**
     * 前端房源分页查询显示
     * @param pageNum : 当前页数
     * @param pageSize : 每页记录数
     * @param houseQueryBo : 搜索条件， 排序条件
     * @return
     */
    @Override
    public PageInfo<HouseVo> findByPage(Integer pageNum, Integer pageSize, HouseQueryBo houseQueryBo) {
        PageHelper.startPage(pageNum, pageSize);
        Page<HouseVo> page = houseMapper.findByPage(houseQueryBo);
        return new PageInfo<>(page);
    }
}
