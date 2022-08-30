package com.atguigu.service.impl;

import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.mapper.UserFollowMapper;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements UserFollowService {
    
    @Autowired
    private UserFollowMapper userFollowMapper;
    
    @Override
    public BaseMapper<UserFollow> getBaseMapper() {
        return userFollowMapper;
    }

    @Override
    public boolean isFollow(Long userId, Long houseId) {
        UserFollow userFollow = userFollowMapper.getByUserIdAndHouseId(userId,houseId);
        return userFollow != null;
    }

    @Override
    public void deleteByUserIdAndHouseId(Long userId, Long houseId) {
        userFollowMapper.deleteByUserIdAndHouseId(userId, houseId);
    }

    /**
     * 我的关注列表分页查询
     * @param pageNum : 页数
     * @param pageSize : 每页记录数
     * @param id : userId
     * @return
     */
    @Override
    public PageInfo<UserFollowVo> findByPage(Integer pageNum, Integer pageSize, Long id) {
        PageHelper.startPage(pageNum, pageSize);
        Page<UserFollowVo> page = userFollowMapper.findByPage(id);
        return new PageInfo<>(page);
    }

    /**
     * 取消关注
     * @param id : user_follow的主键id
     */
    @Override
    public void cancelFollowByKey(Long id) {
        userFollowMapper.delete(id);
    }
}
