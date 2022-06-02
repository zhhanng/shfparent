package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Role;
import com.atguigu.mapper.RoleMapper;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Date:2022/5/17
 * Author:zh
 * Description:
 */
@Transactional
@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService{

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public BaseMapper<Role> getEntityMapper() {
        return roleMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Role> findAll() {
        return roleMapper.findAll();
    }

}
