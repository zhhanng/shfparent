package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.bo.LoginBo;
import com.atguigu.entity.bo.RegisterBo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Date:2022/5/25
 * Author:zh
 * Description:
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Reference
    private UserInfoService userInfoService;

    @Autowired
    JedisPool jedisPool;

    /**
     * 该方法是实现点击获取验证码得到验证码的功能
     *
     * @param mobile
     * @return
     */
    @GetMapping("/sendCode/{mobile}")
    public Result SendCode(@PathVariable("mobile") String mobile, HttpSession session) {
        //本来调用的是阿里云的短信服务
        String code = "1111";
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.setex("code", 60, code);
        } finally {
            if (jedis != null) {
                jedis.close();
                //上边只是归还到连接池,还需要关闭
                if (jedis.isConnected()) {
                    jedis.disconnect();
                }
            }
        }
        return Result.ok();
    }

    /**
     * 实现手机号,验证码校验
     *
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterBo registerBo, HttpSession session) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String sessionCode = jedis.get("code");
            if (!registerBo.getCode().equalsIgnoreCase(sessionCode)) {
                //验证码校验失败
                return Result.build(null, ResultCodeEnum.CODE_ERROR);
            }
        }finally {
            if (jedis!=null){
                jedis.close();
                if (jedis.isConnected()) {
                    jedis.disconnect();
                }
            }
        }

        UserInfo userInfo = userInfoService.getByPhone(registerBo.getPhone());
        if (userInfo != null) {
            return Result.build(null, ResultCodeEnum.PHONE_REGISTER_ERROR);
        }
        //MD5加密
        String encrypt = MD5.encrypt(registerBo.getPassword());
        userInfo = new UserInfo();
        //属性复制赋值
        BeanUtils.copyProperties(registerBo, userInfo);
        userInfo.setPassword(encrypt);
        userInfo.setStatus(1);
        //插入进表
        userInfoService.insert(userInfo);
        return Result.ok();
    }

    /**
     * 这是处理登录请求的方法
     *
     * @param loginBo
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginBo loginBo, HttpSession session) {
        UserInfo userInfo = userInfoService.getByPhone(loginBo.getPhone());
        if (userInfo == null) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
        }
        if (!userInfo.getPassword().equals(MD5.encrypt(loginBo.getPassword()))) {
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
        }
        //判断账号是否被锁定了
        if (userInfo.getStatus() == 0) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }
        //放进session对象
        session.setAttribute("USER", userInfo);
        //jason数据返回让前端回显
        HashMap<String, Object> map = new HashMap<>();
        map.put("nickName", userInfo.getNickName());
        map.put("phone", userInfo.getPhone());
        return Result.ok(map);
    }

    @GetMapping("/logout")
    public Result logout(HttpSession session) {
        session.invalidate();
        return Result.ok();
    }


}
