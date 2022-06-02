package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.en.DictCode;
import com.atguigu.en.HouseStatus;
import com.atguigu.entity.*;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Date:2022/5/23
 * Author:zh
 * Description:
 */
@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {
    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE = "house/create";
    private static final String PAGE_EDIT = "house/edit";
    private static final String LIST_ACTION = "redirect:/house";
    private static final String PAGE_SHOW = "house/show";

    @Reference
    private HouseService houseService;

    @Reference
    private DictService dictService;

    @Reference
    private CommunityService communityService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private HouseUserService houseUserService;

    @Reference
    private HouseImageService houseImageService;

    @RequestMapping
    public String index(@RequestParam Map<String, Object> filters, Model model) {
        //处理pageNum和pageSize为空的情况
        if (!filters.containsKey("pageNum")) {
            filters.put("pageNum", 1);
        }
        if (!filters.containsKey("pageSize")) {
            filters.put("pageSize", 10);
        }
        //1. 分页搜索房源列表信息
        PageInfo<House> pageInfo = houseService.findPage(filters);

        //2. 将房源分页信息存储到请求域
        model.addAttribute("page", pageInfo);

        //3. 将搜索条件存储到请求域
        model.addAttribute("filters", filters);

        //4. 查询所有小区、以及字典里的各种列表存储到请求域
        saveAllDictToRequestScope(model);

        return PAGE_INDEX;
    }

    /**
     * 查询所有小区以及字典里的各种列表存储到请求域
     *
     * @param model
     */
    private void saveAllDictToRequestScope(Model model) {
        //2. 查询所有小区
        List<Community> communityList = communityService.findAll();
        //3. 查询各种初始化列表:户型列表、楼层列表、装修情况列表....
        List<Dict> houseTypeList = dictService.findDictListByParentDictCode(DictCode.HOUSETYPE.getMessage());
        List<Dict> floorList = dictService.findDictListByParentDictCode(DictCode.FLOOR.getMessage());
        List<Dict> buildStructureList = dictService.findDictListByParentDictCode(DictCode.BUILDSTRUCTURE.getMessage());
        List<Dict> directionList = dictService.findDictListByParentDictCode(DictCode.DIRECTION.getMessage());
        List<Dict> decorationList = dictService.findDictListByParentDictCode(DictCode.DECORATION.getMessage());
        List<Dict> houseUseList = dictService.findDictListByParentDictCode(DictCode.HOUSEUSE.getMessage());
        //5. 将所有小区存储到请求域
        model.addAttribute("communityList", communityList);
        //6. 将各种列表存储到请求域
        model.addAttribute("houseTypeList", houseTypeList);
        model.addAttribute("floorList", floorList);
        model.addAttribute("buildStructureList", buildStructureList);
        model.addAttribute("directionList", directionList);
        model.addAttribute("decorationList", decorationList);
        model.addAttribute("houseUseList", houseUseList);
    }

    /**
     * 添加房源信息.create页面
     *
     * @param house
     * @param model
     * @return
     */
    @RequestMapping("/create")
    public String create(House house, Model model) {
        saveAllDictToRequestScope(model);
        return PAGE_CREATE;
    }

    /**
     * 添加房源信息,保存页面,特别需要注意房屋的状态信息
     * ,发布和未发布
     *
     * @param house
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(House house, Model model) {
        house.setStatus(HouseStatus.UNPUBLISHED.code);
        houseService.insert(house);
        return successPage(model, "房源信息添加成功");
    }

    /**
     * 修改中的回显
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        House house = houseService.getById(id);
        model.addAttribute("house", house);
        saveAllDictToRequestScope(model);
        return PAGE_EDIT;
    }

    /**
     * 修改中的保存更新
     *
     * @param house
     * @param model
     * @return
     */
    @PostMapping("/update")
    public String update(House house, Model model) {
        houseService.update(house);
        return successPage(model, "房源信息修改成功");
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        houseService.delete(id);
        return LIST_ACTION;
    }

    @RequestMapping("/publish/{id}/{status}")
    public String publish(@PathVariable("id") Long id, @PathVariable("status") Integer status, Model model) {
        houseService.publish(id, status);
        return LIST_ACTION;
    }

    @GetMapping("/{houseId}")
    public String details(@PathVariable("houseId") Long houseId, Model model) {
        //房子的详情
        House house = houseService.getById(houseId);
        Community community = communityService.getById(house.getCommunityId());

        //查看图片,房产和房源
        List<HouseImage> houseImage1List = houseImageService.findHouseImageList(houseId, 1l);
        List<HouseImage> houseImage2List = houseImageService.findHouseImageList(houseId, 2l);

        //查看经纪人
        List<HouseBroker> houseBrokerList = houseBrokerService.findHouseBrokerListByHouseId(houseId);

        //产出房东列表
        List<HouseUser> houseUserList = houseUserService.findHouseUserListByHouseId(houseId);

        //将上述查询到的内容存储到请求域
        model.addAttribute("house",house);
        model.addAttribute("community",community);
        model.addAttribute("houseImage1List",houseImage1List);
        model.addAttribute("houseImage2List",houseImage2List);
        model.addAttribute("houseBrokerList",houseBrokerList);
        model.addAttribute("houseUserList",houseUserList);

        return PAGE_SHOW;
    }
}
