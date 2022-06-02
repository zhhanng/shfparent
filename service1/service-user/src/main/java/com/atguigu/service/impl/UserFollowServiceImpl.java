package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.mapper.UserFollowMapper;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Date:2022/5/25
 * Author:zh
 * Description:
 */
@Service(interfaceClass = UserFollowService.class)
public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements
        UserFollowService
{
    @Autowired
    private UserFollowMapper userFollowMapper;
    @Override
    public List<UserFollow> findAll() {
        return null;
    }

    @Override
    public BaseMapper<UserFollow> getEntityMapper() {
        return userFollowMapper;
    }

    @Override
    public PageInfo<UserFollowVo> findListPage(int pageNum, int pageSize, Long userId) {
       PageHelper.startPage(pageNum, pageSize);
       return new PageInfo<>(userFollowMapper.findListPage(userId),10);
    }


    @Override
    public UserFollow findByUserIdAndHouseId(Long userId,Long houseId) {
        return userFollowMapper.findByUserIdAndHouseId(userId,houseId);
    }
}
