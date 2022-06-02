package com.atguigu.helper;

import com.atguigu.entity.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装的树状权限图
 * Date:2022/5/28
 * Author:zh
 * Description:
 */
public class PermissionHelper {
    //树状权限图,可以理解为快速排序
    public static List<Permission> build(List<Permission> originalList) {
        List<Permission> permissionList = new ArrayList<>();
        for (Permission permission : originalList) {
            //如果其父节点的id是0的话说明该permission是一级菜单
            if (permission.getParentId() == 0) {
                permission.setLevel(1);//设置其等级是1
                permission.setChildren(getChildren(permission, originalList));//设置其子菜单
                permissionList.add(permission);//放入集合中
            }
        }
        return permissionList;
    }

    public static List<Permission> getChildren(Permission permission, List<Permission> permissionList) {
        ArrayList<Permission> ChildrenList = new ArrayList<>();
        for (Permission permission1 : permissionList) {
            //权限的父ID和所传入节点比较
            if (permission1.getParentId().equals(permission.getId())) {
                //设置权限的等级
                permission1.setLevel(permission.getLevel() + 1);
                //设置权限的子权限
                permission1.setChildren(getChildren(permission1, permissionList));
                //设置权限的父权限名称
                permission1.setParentName(permission.getName());
                ChildrenList.add(permission1);
            }
        }
        return ChildrenList;
    }
}
