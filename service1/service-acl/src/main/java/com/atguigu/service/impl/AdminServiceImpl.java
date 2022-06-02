package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Admin;
import com.atguigu.entity.AdminRole;
import com.atguigu.mapper.AdminMapper;
import com.atguigu.mapper.AdminRoleMapper;
import com.atguigu.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2022/5/19
 * Author:zh
 * Description:
 */
@Service(interfaceClass = AdminService.class)
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;



    @Override
    public BaseMapper<Admin> getEntityMapper() {
        return adminMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Admin> findAll() {
        return adminMapper.findAll();
    }

    @Override
    public Admin getByUsername(String username) {
        return adminMapper.getByUserName(username);
    }
}
