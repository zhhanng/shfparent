package com.atguigu.interceptor;

import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.util.WebUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Handler;

/**
 * Date:2022/5/25
 * Author:zh
 * Description:登录校验拦截器,没登陆的话禁止使用相关功能
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getSession().getAttribute("USER")==null){
            WebUtil.writeJSON(response, Result.build(null, ResultCodeEnum.LOGIN_AUTH));
            return false;
        }
        return true;
    }
}
