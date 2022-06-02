package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;

import java.util.List;
import java.util.Map;

public interface AdminRoleService extends BaseService<AdminRole> {
    Map<String, List<Role>> findRolesByAdminId(Long adminId);

    void saveAdminRole(Long adminId, List<Long> uploadRoleList);
}
