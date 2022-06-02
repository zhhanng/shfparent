package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Date:2022/5/22
 * Author:zh
 * Description:
 */
@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {
    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;
    private final static String PAGE_INDEX = "community/index";
    private static final String PAGE_CREATE = "community/create";
    private static final String PAGE_EDIT = "community/edit";
    private static final String LIST_ACTION = "redirect:/community";

    @RequestMapping
    public String index(@RequestParam Map<String, Object> filters, Model model) {
        if (filters.get("pageNum") == null || "".equals(filters.get("pageNum"))) {
            filters.put("pageNum", 1);
        }
        if (filters.get("pageSize") == null || "".equals(filters.get("pageSize"))) {
            filters.put("pageSize", 10);
        }

        if (!filters.containsKey("areaId")) {
            filters.put("areaId", "");
        }
        if (!filters.containsKey("plateId")) {
            filters.put("plateId", "");
        }

        PageInfo<Community> page = communityService.findPage(filters);
        List<Dict> areaList = dictService.findDictListByParentDictCode("beijing");
        model.addAttribute("page", page);
        model.addAttribute("areaList", areaList);
        model.addAttribute("filters", filters);
        return PAGE_INDEX;
    }

    @RequestMapping("/create")
    public String ctreate(Model model) {
        List<Dict> beijing = dictService.findDictListByParentDictCode("beijing");
        model.addAttribute("areaList", beijing);
        return PAGE_CREATE;
    }

    @PostMapping("/save")
    public String save(Community community,Model model){
        //调用业务层的方法保存小区数据
        communityService.insert(community);
        return successPage(model,"保存小区信息成功");
    }

    /**
     * 在回显小区
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id")Long id,Model model){
        Community community = communityService.getById(id);
        //查询北京的所有区域,用户可以改变小区的区域
        List<Dict> areaList = dictService.findDictListByParentDictCode("beijing");
        //将查询到的小区信息以及areaList存储到请求域
        model.addAttribute("areaList",areaList);
        model.addAttribute("community",community);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(Community community,Model model){
        communityService.update(community);
      return successPage(model,"修改小区信息成功");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        //调用业务层的方法根据id删除小区信息
        communityService.delete(id);
        return LIST_ACTION;
    }
}
