package com.atguigu.confing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 包名:com.atguigu.config
 *
 * @author Leevi
 * 日期2022-05-28  11:28
 * 1. 继承WebSecurityConfigurerAdapter
 * 2. 配置类的要求: @Configuration注解
 * 3. 开启SpringSecurity: 添加@EnableWebSecurity注解
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //在内存中保存用户名和密码
        auth.inMemoryAuthentication()
                .withUser("lucy")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("");
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //允许页面嵌套
        http.headers().frameOptions().disable();
        //自定义配置
        http
            .authorizeRequests()
            .antMatchers("/static/**","/login").permitAll()  //允许匿名用户访问的路径
            .anyRequest().authenticated()    // 其它页面全部需要验证
            .and()
            .formLogin()
            .loginPage("/login")    //用户未登录时，访问任何需要权限的资源都转跳到该路径，即登录页面，此时登陆成功后会继续跳转到第一次访问的资源页面（相当于被过滤了一下）
            .defaultSuccessUrl("/") //登录认证成功后默认转跳的路径
            .and()
            .logout()
            .logoutUrl("/logout")   //退出登陆的路径，指定spring security拦截的注销url,退出功能是security提供的
            .logoutSuccessUrl("/login");//用户退出后要被重定向的url

        //跨域:从一个域名发请求访问另一个域名
        //关闭跨域请求伪造
        http.csrf().disable();
        http.exceptionHandling().accessDeniedHandler(new AtguiguDeniedHandler());
    }


    @Bean
    public PasswordEncoder createPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
