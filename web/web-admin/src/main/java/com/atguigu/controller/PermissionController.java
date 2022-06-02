package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Permission;
import com.atguigu.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Date:2022/5/28
 * Author:zh
 * Description:
 */

@Controller
@RequestMapping("permission")
public class PermissionController extends BaseController {

    private static final String PAGE_CREATE = "permission/create";
    private static final String PAGE_ERROR = "common/errorPage";
    private static final String PAGE_EDIT = "permission/edit";
    private static final String LIST_ACTION = "redirect:/permission";
    @Reference
    private PermissionService permissionService;

    private static final String PAGE_INDEX = "permission/index";

    @GetMapping
    public String index(Model model){
        List<Permission> permissionList = permissionService.findAllMenu();
        model.addAttribute("list",permissionList);
        return PAGE_INDEX;
    }

    /**
     * 这个新增的方法不能添加@RequestParam注解
     * @param permission
     * @param model
     * @return
     */
    @GetMapping("/create")
    public String create(Permission permission, Model model){
        model.addAttribute(permission);
        return PAGE_CREATE;
    }

    @PostMapping("/save")
    public String save(Permission permission,Model model){
        permissionService.insert(permission);
        return successPage(model,"添加菜单成功");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id")Long id,Model model){
        try {
            permissionService.delete(id);
            return LIST_ACTION;
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erroeMessage",e.getMessage());
            return PAGE_ERROR;
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id")Long id,Model model){
        Permission permission = permissionService.getById(id);
        model.addAttribute("permission",permission);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update( Permission permission,Model model){
        permissionService.update(permission);
        return successPage(model,"修改数据成功");
    }
}
