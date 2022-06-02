package com.atguigu.base;

import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InterfaceAddress;
import java.util.List;
import java.util.Map;

public interface BaseService<T> {

    void insert(T t);

    List<T> findAll();

    T getById(Long id);

    void delete(Long id);

    PageInfo<T> findPage(Map<String, Object> filters);

    void update(T t);

}

