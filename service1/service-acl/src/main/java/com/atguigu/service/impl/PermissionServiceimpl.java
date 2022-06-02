package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Permission;
import com.atguigu.entity.RolePermission;
import com.atguigu.helper.PermissionHelper;
import com.atguigu.mapper.PermissionMapper;
import com.atguigu.mapper.RolePermissionMapper;
import com.atguigu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = PermissionService.class)
public class PermissionServiceimpl extends BaseServiceImpl<Permission> implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;


    @Override
    public List<Permission> findAll() {
        return null;
    }

    @Override
    public BaseMapper<Permission> getEntityMapper() {
        return permissionMapper;
    }

    /**
     * 先回显角色的权限,和查询用户的角色的区别在于权限需要变成属性,因此需要
     * id,parentI等等一系列属性.需要实现的效果,展示全部权限和已分配的权限,已分配的权限
     * 需要打勾.
     *
     * @param roleId
     * @return
     */
    @Override
    public List<Map<String, Object>> findPermissionByRoleId(Long roleId) {
        //所有权限
        List<Permission> allPermissionList = permissionMapper.findAll();
        //已分配的权限
        List<Long> assignPermission = rolePermissionMapper.findPermissionListByRoleId(roleId);
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();

        for (Permission permission : allPermissionList) {
            HashMap<String, Object> map = new HashMap<>();
            if (assignPermission.contains(permission.getId())) {
                //包含说明分配
                map.put("checked", true);
            } else {
                map.put("checked", false);
            }
            map.put("id", permission.getId());
            map.put("pId", permission.getParentId());
            map.put("name", permission.getName());
            map.put("open", true);
            arrayList.add(map);
        }

        return arrayList;
    }

    @Override
    public void saveRolePermission(Long roleId, List<Long> permissionIds) {
        List<Long> allPermissions = rolePermissionMapper.findPermissionListByRoleId(roleId);
        List<Long> needRemove = new ArrayList<>();
        for (Long permission : allPermissions) {
            //不包含就是已删除的
            if (!permissionIds.contains(permission)) {
                needRemove.add(permission);
            }
        }
        if (needRemove != null && needRemove.size() > 0) {
            rolePermissionMapper.remove(roleId, needRemove);
        }
        for (Long permissionId : permissionIds) {
            RolePermission rolePermission = rolePermissionMapper.findByRoleIdAndPermissionId(roleId, permissionId);
            //没有过此绑定,需要新插入
            if (rolePermission == null) {
                rolePermission = new RolePermission();
                rolePermission.setPermissionId(permissionId);
                rolePermission.setRoleId(roleId);
                rolePermissionMapper.insert(rolePermission);
            } else {
                if (rolePermission.getIsDeleted() == 1) {
                    //说明以前赋予了权限后"删除"
                    rolePermission.setIsDeleted(0);
                    rolePermissionMapper.update(rolePermission);
                }
            }
        }
    }

    @Override
    public List<Permission> findMenuPermissionByAdminId(Long adminId) {
        List<Permission> permissionList = new ArrayList<Permission>();
        if (adminId == 1) {
            //如果adminId是1,那么就为管理员,直接查找所有的权限
            permissionList = permissionMapper.findAll();

        } else {
            permissionList = permissionMapper.findPermissionListByAdminId(adminId);
        }
        return PermissionHelper.build(permissionList);
    }

    @Override
    public List<Permission> findAllMenu() {
        List<Permission> permissionList = permissionMapper.findAll();
        return PermissionHelper.build(permissionList);
    }

    @Override
    public void delete(Long id) {
        Long count = permissionMapper.getCildrenCount(id);
        if (count > 0) {
            throw new RuntimeException("当前菜单有子菜单，不能删除");
        } else {
            permissionMapper.delete(id);
        }
    }

    @Override
    public List<String> findcodePermissionByAdminId(Long adminId) {
        if (adminId == 1) {
            return permissionMapper.findAllCodePermission();
        } else {
            return permissionMapper.findCodePermissionByAdminId(adminId);
        }
    }
}
