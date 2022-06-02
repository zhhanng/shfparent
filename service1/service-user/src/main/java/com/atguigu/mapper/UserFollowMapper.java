package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;


public interface UserFollowMapper extends BaseMapper<UserFollow> {
    UserFollow findByUserIdAndHouseId(@Param("userId") Long userId, @Param("houseId") Long houseId);

    //需要实现分页功能,要返回一个代表用户关注房源的id,即userFolllow的id列
    Page<UserFollowVo> findListPage(@Param("userId") Long userId);
}
