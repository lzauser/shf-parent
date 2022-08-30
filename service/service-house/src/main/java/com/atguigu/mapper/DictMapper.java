package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Dict;

import java.util.List;

public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 根据parentId获取Dict信息
     * @param parentId
     * @return
     */
    List<Dict> findListByParentId(Long parentId);


    /**
     * 根据parentId统计Ditc个数
     * @param parentId
     * @return
     */
    Long countByParentId(Long parentId);

    /**
     * 根据id获取名称
     * @param id
     * @return
     */
    String getNameById(Long id);


    /**
     * 根据dictCode获取id
     * @param code : 编码
     * @return : 数据字典id
     */
    Long getIdByCode(String code);
}
