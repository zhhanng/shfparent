package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Role;
import com.atguigu.service.AdminRoleService;
import com.atguigu.service.AdminService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Date:2022/5/19
 * Author:zh
 * Description:
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    private static final String PAGE_ASSIGN_SHOW = "admin/assignShow";
    @Reference
    private AdminService adminService;

    @Reference
    private AdminRoleService adminRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String PAGE_UPLOAD_SHOW = "admin/upload";
    private final static String PAGE_INDEX = "admin/index";
    private final static String LIST_ACTION = "redirect:/admin";
    private final static String PAGE_EDIT = "admin/edit";
    @RequestMapping
    public String index(@RequestParam Map<String,Object> filters, Model model){
        if(!filters.containsKey("pageNum")){
            filters.put("pageNum",1);
        }
        if(!filters.containsKey("pageSize")){
            filters.put("pageSize",10);
        }
        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        model.addAttribute("page",pageInfo);
        model.addAttribute("filters",filters);
        return PAGE_INDEX;
    }

    @PostMapping("/save")
    public String save(Admin admin, Model model){
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminService.insert(admin);
        return successPage(model,"新增用户成功");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        adminService.delete(id);
        return LIST_ACTION;
    }

    @GetMapping("/edit/{id}")
    public String save(Model model, @PathVariable("id") Long id) {
        Admin admin = adminService.getById(id);
        model.addAttribute("admin",admin);
        return PAGE_EDIT;
    }

    @GetMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable("id") Long id,Model model){
        model.addAttribute("id",id);
        return PAGE_UPLOAD_SHOW;
    }

    @PostMapping("/upload/{id}")
    public String upload(@PathVariable("id") Long id,
                         @RequestParam("file")MultipartFile multipartFile,
                         Model model) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String uuidName = FileUtil.getUUIDName(originalFilename);
        QiniuUtils.upload2Qiniu(multipartFile.getBytes(),uuidName);

        Admin admin = new Admin();
        admin.setId(id);
        admin.setHeadUrl("http://rcdm9i8kn.hn-bkt.clouddn.com/"+uuidName);

        adminService.update(admin);

        return successPage(model,"头像上传成功");

    }

    @GetMapping("/assignShow/{adminId}")
    public String assignShow(@PathVariable("adminId")Long adminId,Model model){
        Map<String, List<Role>> roleList = adminRoleService.findRolesByAdminId(adminId);
        model.addAllAttributes(roleList);
        return PAGE_ASSIGN_SHOW;
    }

    @PostMapping("/assignRole")
    public String assignRole(@RequestParam("adminId")Long adminId,
                             @RequestParam("roleIds") List<Long> roleIds,
                             Model model)
    {
        adminRoleService.saveAdminRole(adminId,roleIds);
        return successPage(model,"分配角色已保存");
    }
}
