package com.atguigu.base;

import com.github.pagehelper.Page;

import java.util.Map;

/**
 * Date:2022/5/19
 * Author:zh
 * Description:
 */
public interface BaseMapper<T> {
    void insert(T t);
    T getById(Long id);
    void update(T t);
    void delete(Long id);
    /**
     * 分页查询
     * @param filters
     * @return
     */
    Page<T> findPage(Map<String,Object> filters);
}
