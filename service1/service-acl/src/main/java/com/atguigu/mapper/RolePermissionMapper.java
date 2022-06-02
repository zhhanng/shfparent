package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Permission;
import com.atguigu.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    List<Long> findPermissionListByRoleId(@Param("roleId")Long roleId);

    RolePermission findByRoleIdAndPermissionId(@Param("roleId")Long roleId, @Param("permissionId")Long permissionId);

    void insert(RolePermission rolePermission);

    void update(RolePermission rolePermission);

    void remove(@Param("roleId")Long roleId,@Param("needRemoveList") List<Long> needRemoveList);

    List<Permission> findAll();
}
