package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

public interface UserFollowService extends BaseService<UserFollow> {
    /**
     * 判断是否关注
     * @param userId : 用户id
     * @param houseId : 房源id
     * @return
     */
    boolean isFollow(Long userId, Long houseId);

    /**
     * 根据userId和houseId取消关注(直接从数据库中删除)
     * @param userId
     * @param id
     */
    void deleteByUserIdAndHouseId(Long userId, Long id);

    /**
     * 分页查询关注列表
     * @param pageNum
     * @param pageSize
     * @param id
     * @return
     */
    PageInfo<UserFollowVo> findByPage(Integer pageNum, Integer pageSize, Long id);

    /**
     * 取消关注
     * @param id : user_follow的主键id
     * @return
     */
    void cancelFollowByKey(Long id);
}
