package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {
    List<Permission> findAll();
    /**
     * 通过adminId查找Permission
     * @param adminId
     * @return
     */
    List<Permission> findPermissionListByAdminId(Long adminId);

    Long getCildrenCount(Long id);

    /**
     * 查看用户的权限,表中的code字段
     * @param adminId
     * @return
     */
    List<String> findCodePermissionByAdminId(Long adminId);

    /**
     * 管理员专用,查看所有的权限
     * @return
     */
    List<String> findAllCodePermission();

}
