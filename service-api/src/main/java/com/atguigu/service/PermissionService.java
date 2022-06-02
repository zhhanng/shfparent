package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Permission;

import javax.xml.ws.Service;
import java.util.List;
import java.util.Map;

public interface PermissionService extends BaseService<Permission> {
    List<Map<String,Object>> findPermissionByRoleId(Long roleId );

    void saveRolePermission(Long roleId, List<Long> permissionIds);

    List<Permission> findMenuPermissionByAdminId(Long adminId);

    List<Permission> findAllMenu();

    List<String> findcodePermissionByAdminId(Long adminId);
}
