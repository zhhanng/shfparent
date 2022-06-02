package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.AdminRole;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

public interface AdminRoleMapper extends BaseMapper<AdminRole> {
    List<Long> findRolesByAdminId(Long AdminId);

    AdminRole findByRoleIdAndAdminId(@Param("adminId") Long adminId,@Param("roleId") Long RoleId);

    void update(AdminRole adminRole);

    void removeRole(@Param("adminId") Long adminId, @Param("needRemoveList") ArrayList<Long> needRemoveList);

    void insert(AdminRole adminRole);
}
