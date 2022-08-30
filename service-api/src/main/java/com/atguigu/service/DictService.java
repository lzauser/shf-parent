package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Dict;

import java.util.List;
import java.util.Map;

public interface DictService extends BaseService<Dict> {
    /**
     * 根据parentId获取zTree节点数据
     * @param parentId ： parentId
     * @return
     */
    List<Map<String, Object>> findZnodesByParentId(Long parentId);

    /**
     * 根据parentId获取Dict信息
     * @param parentId
     * @return
     */
    List<Dict> findListByParentId(Long parentId);

    /**
     * 根据parentCode获取Dict信息
     * @param parentCode
     * @return
     */
    List<Dict> findListByParentCode(String parentCode);
    
}
