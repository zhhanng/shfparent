package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.mapper.AdminRoleMapper;
import com.atguigu.mapper.RoleMapper;
import com.atguigu.service.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date:2022/5/27
 * Author:zh
 * Description:
 */
@Service(interfaceClass = AdminRoleService.class)
public class AdminRoleServiceImpl extends BaseServiceImpl<AdminRole> implements AdminRoleService {

    @Autowired
   private AdminRoleMapper adminRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public BaseMapper<AdminRole> getEntityMapper() {
        return adminRoleMapper;
    }

    @Override
    public Map<String, List<Role>> findRolesByAdminId(Long adminId) {
        //查询所有的角色列表
        List<Role> allRoles = roleMapper.findAll();
        //查询已分配的角色列表的id,这是联合表中查询的
        List<Long> assignedRoleId = adminRoleMapper.findRolesByAdminId(adminId);
        ArrayList<Role> assignRoleList = new ArrayList<>();
        ArrayList<Role> unAssignRoleList = new ArrayList<>();
        /**
         * 将已分配的角色和未分配的角色分别存储到两个集合
         */
        for (Role role : allRoles) {
            if(assignedRoleId.contains(role.getId())){
                assignRoleList.add(role);
            }else {
                unAssignRoleList.add(role);
            }
        }

        HashMap<String, List<Role>> listHashMap = new HashMap<>();
        listHashMap.put("unAssignRoleList",unAssignRoleList);
        listHashMap.put("assignRoleList",assignRoleList);
        return listHashMap;
    }

    @Override
    public List<AdminRole> findAll() {
        return null;
    }

    /*
    保存用户的角色分配
     */
    @Override
    public void saveAdminRole(Long adminId, List<Long> uploadRoleList) {
        List<Long> allRoleList = adminRoleMapper.findRolesByAdminId(adminId);
        //需要移除的角色列表
        ArrayList<Long> needRemoveList = new ArrayList<>();
        for (Long roleId : allRoleList) {
            //遍历所有角色,如果上传的列表中不含角色就说明已删除
            //搞清楚是谁不包含谁,上传的不包含所有的
            if (!uploadRoleList.contains(roleId)) {
                needRemoveList.add(roleId);
            }
        }
        if (needRemoveList != null && needRemoveList.size() > 0) {
            adminRoleMapper.removeRole(adminId, needRemoveList);
        }
        /*
        添加新增的角色,分两点,如果以前有过权限后移除就直接改变is_deleted选项,否则就在中间表中新增一个
        绑定
         */
        for (Long uploadRoleId : uploadRoleList) {
            AdminRole adminRole = adminRoleMapper.findByRoleIdAndAdminId(adminId, uploadRoleId);
            //说明该角色时第一次赋予
            if (adminRole == null) {
                 adminRole = new AdminRole();
                adminRole.setIsDeleted(0);
                adminRole.setAdminId(adminId);
                adminRole.setRoleId(uploadRoleId);
                adminRoleMapper.insert(adminRole);
            } else {
                //该角色不是第一次赋予,只需要改变is_deleted字段就可以
                if (adminRole.getIsDeleted() == 1) {
                    adminRole.setIsDeleted(0);
                    adminRoleMapper.update(adminRole);
                }
            }
        }
    }
}
