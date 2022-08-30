package com.atguigu.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Dict;
import com.atguigu.mapper.DictMapper;
import com.atguigu.service.DictService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private JedisPool jedisPool;

    @Override
    public BaseMapper<Dict> getBaseMapper() {
        return dictMapper;
    }

    /**
     * 根据parentId获取zTree节点数据
     * @param parentId ： parentId
     * @return
     */
    @Override
    public List<Map<String, Object>> findZnodesByParentId(Long parentId) {
        List<Dict> dictList = findListByParentId(parentId);
        List<Map<String, Object>> zNodes = new ArrayList<>();
        for (Dict dict : dictList) {
            HashMap<String, Object> zNode = new HashMap<>();
            zNode.put("id", dict.getId());
            zNode.put("name", dict.getName());
            zNode.put("isParent", dictMapper.countByParentId(dict.getId()) > 0 );
            zNodes.add(zNode);
        }
        return zNodes;
    }

    /**
     * 根据parentId获取区域列表
     * @param parentId
     * @return
     */
    @Override
    public List<Dict> findListByParentId(Long parentId) {
        Jedis jedis = jedisPool.getResource();
        String key = "shf:dict:parentid:" + parentId;

        String jsonStr = jedis.get(key);
        if (null != jsonStr) {
            //redis有dictList，解析json字符串
            List<Dict> dictList = JSON.parseObject(jsonStr,List.class);
            return dictList;
        }
        //redis没有dictList，查询数据库，并存储到redis
        List<Dict> dictList = dictMapper.findListByParentId(parentId);

        //string
        jedis.set(key, JSON.toJSONString(dictList));

        jedis.close();

        return dictList;
    }

    @Override
    public List<Dict> findListByParentCode(String parentCode) {
        //根据dictCode获取数据字典id
        Long parentId = dictMapper.getIdByCode(parentCode);
        return findListByParentId(parentId);
    }


}
