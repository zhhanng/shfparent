package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Date:2022/5/17
 * Author:zh
 * Description:
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController extends BaseController {

    private static final String PAGE_ASSIGN_SHOW = "role/assignShow";
    @Reference
    private RoleService roleService;
    @Reference
    private PermissionService permissionService;
    //配置的前缀是WEB-INF/trmplates/以/结尾,因此逻辑视图就不加/
    private final static String PAGE_INDEX = "role/index";
    private static final String LIST_ACTION = "redirect:/role";
    private static final String PAGE_SUCCESS = "common/successPage";
    private static final String PAGE_EDIT = "role/edit";

    @RequestMapping
    public String index(@RequestParam Map filters, Model model) {
        if (!filters.containsKey("pageNum")) {
            filters.put("pageNum", 1);
        }
        if (!filters.containsKey("pageSize")) {
            filters.put("pageSize", 10);
        }
        PageInfo pageInfo = roleService.findPage(filters);
        model.addAttribute("page", pageInfo);
        model.addAttribute("filters", filters);
        return PAGE_INDEX;
    }
    @PreAuthorize("hasAnyAuthority('role.create')")
    @PostMapping("/save")
    public String save(Role role, Model model) {
        roleService.insert(role);
        return successPage(model, "新增角色成功");
    }
    @PreAuthorize("hasAnyAuthority('role.edit')")
    @GetMapping("/edit/{id}")
    public String save(Model model, @PathVariable Long id) {
        Role role = roleService.getById(id);
        model.addAttribute("role", role);
        return PAGE_EDIT;
    }
    @PreAuthorize("hasAnyAuthority('role.edit')")
    @PostMapping(value = "/update")
    public String update(Role role, Model model) {
        roleService.update(role);
        return successPage(model, "更新角色成功");
    }
    @PreAuthorize("hasAnyAuthority('role.delete')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        roleService.delete(id);
        return LIST_ACTION;
    }
    @PreAuthorize("hasAnyAuthority('role.assign')")
    @GetMapping("/assignShow/{roleId}")
    public String assignShow(@PathVariable("roleId") Long roleId,
                             Model model) {
        model.addAttribute("roleId", roleId);
        List<Map<String, Object>> zNodes = permissionService.findPermissionByRoleId(roleId);
        model.addAttribute("zNodes", JSON.toJSONString(zNodes));
        return PAGE_ASSIGN_SHOW;
        }


@PreAuthorize("hasAnyAuthority('role.assign')")
@PostMapping("/assignPermission")
public String assignPermission(@RequestParam("roleId") Long roleId,
@RequestParam("permissionIds") List<Long> permissionIds,
        Model model) {
        permissionService.saveRolePermission(roleId,permissionIds);
        return successPage(model,"角色权限修改成功");
        }

        }
