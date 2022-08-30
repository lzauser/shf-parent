package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

public interface UserFollowMapper extends BaseMapper<UserFollow> {
    /**
     * 根据userId和houseId取消关注(直接从数据库中删除)
     * @param userId
     * @param houseId
     */
    void deleteByUserIdAndHouseId(@Param("userId") Long userId, @Param("houseId") Long houseId);

    UserFollow getByUserIdAndHouseId(@Param("userId") Long userId, @Param("houseId") Long houseId);

    /**
     * 分页查询房源列表
     * @param id
     * @return
     */
    Page<UserFollowVo> findByPage(Long id);
}
