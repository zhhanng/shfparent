package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;

import java.util.List;

/**
 * Date:2022/5/17
 * Author:zh
 * Description:
 */
public interface RoleService extends BaseService<Role> {
    List<Role> findAll();
}
