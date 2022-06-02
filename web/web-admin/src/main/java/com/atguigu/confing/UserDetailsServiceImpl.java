package com.atguigu.confing;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 包名:com.atguigu.config
 *
 * @author Leevi
 * 日期2022-05-28  14:05
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Reference
    private AdminService adminService;

    @Reference
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1. 根据用户到acl_admin表查找用户,利用用户的名称和密码和security加密后的密码进行比较
        //不需要security去查找数据库.
        Admin admin = adminService.getByUsername(username);
        //2. 判断admin是否为null
        if (admin == null) {
            //用户名错误
            throw new UsernameNotFoundException("用户名错误");
        }
        //校验密码
        //参数1:用户名
        //参数2:密码
        //参数3:当前用户所拥有的权限集合
        List<String> codePermissionList = permissionService.findcodePermissionByAdminId(admin.getId());

        ArrayList<GrantedAuthority> grantedAuthority = new ArrayList<>();
        for (String codePermission : codePermissionList) {
            if(codePermission != null){
                grantedAuthority.add(new SimpleGrantedAuthority(codePermission));
            }
        }
        return new User(username,admin.getPassword(),
                grantedAuthority);
    }
}
