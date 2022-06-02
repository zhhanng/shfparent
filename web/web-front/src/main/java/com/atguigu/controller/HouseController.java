package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.*;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date:2022/5/24
 * Author:zh
 * Description:
 */
@RestController
@RequestMapping("/house")
public class HouseController {

    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private UserFollowService userFollowService;

    @PostMapping("/list/{pageNum}/{pageSize}")
    public Result findListPage(@RequestBody HouseQueryBo houseQueryBo,
                               @PathVariable("pageNum") Integer pageNum,
                               @PathVariable("pageSize") Integer pageSize) {

        PageInfo<HouseVo> pageInfo = houseService.findListPage(pageNum, pageSize, houseQueryBo);
        return Result.ok(pageInfo);
    }

    /**
     * 查看房源详情,id为房源id
     *
     * @param id
     * @param session
     * @return
     */
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id, HttpSession session) {
        House house = houseService.getById(id);
        Community community = communityService.getById(house.getCommunityId());
        List<HouseBroker> broketlist = houseBrokerService.findHouseBrokerListByHouseId(id);
        List<HouseImage> houseImageList = houseImageService.findHouseImageList(id, 1l);
        Map map = new HashMap<String, Object>();
        //需要实现用户是否关注房源功能
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        boolean isFollow = false;
        //判断用户是否登录,下面的方法核心就是判断用户id和houseid是否已经绑定
        if (userInfo != null) {
            UserFollow userFollow = userFollowService.findByUserIdAndHouseId(userInfo.getId(), id);
            if (userFollow != null && userFollow.getIsDeleted() == 0) {
                isFollow = true;
            }
        }
        map.put("house", house);
        map.put("community", community);
        map.put("houseBrokerList", broketlist);
        map.put("houseImage1List", houseImageList);
        map.put("isFollow", isFollow);
        return Result.ok(map);
    }
}
