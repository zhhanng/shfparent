package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Date:2022/5/25
 * Author:zh
 * Description:
 */
@RestController
@RequestMapping("/userFollow")
public class UserFollowController {

    @Reference
    private UserFollowService userFollowService;

    @GetMapping("/auth/follow/{houseId}")
    public Result addFollow(@PathVariable("houseId") Long houseId, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        UserFollow userFollow = userFollowService.findByUserIdAndHouseId(userInfo.getId(), houseId);
         /*此时userfollow不为空意味着以前关注过后来又取消了,数据库里边有相应的表项
         is_deleted=为0,代表删除,此时将该列置为1就可以了
          */
        if (userFollow != null) {
            userFollow.setIsDeleted(0);
            userFollowService.update(userFollow);
        } else {
            //如果为空,则需要新建插入
            userFollow = new UserFollow();
            userFollow.setHouseId(houseId);
            userFollow.setUserId(userInfo.getId());
            userFollow.setIsDeleted(0);
            userFollowService.insert(userFollow);
        }
        return Result.ok();
    }

    @GetMapping(value = "/auth/list/{pageNum}/{pageSize}")
    public Result findListPage(@PathVariable("pageNum") Integer pageNum,
                               @PathVariable("pageSize") Integer pageSize,
                               HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        PageInfo<UserFollowVo> listPage = userFollowService.findListPage(pageNum, pageSize, userInfo.getId());
        return Result.ok(listPage);
    }

    @GetMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable("id") Long id){
        UserFollow userFollow = new UserFollow();
        userFollow.setId(id);
        userFollow.setIsDeleted(1);
        userFollowService.update(userFollow);
        return Result.ok();
    }
}
