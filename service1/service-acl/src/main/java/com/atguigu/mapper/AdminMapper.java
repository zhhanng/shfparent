package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Admin;

import java.util.List;

public interface AdminMapper extends BaseMapper<Admin> {
    List<Admin> findAll();


    Admin getByUserName(String username);
}
