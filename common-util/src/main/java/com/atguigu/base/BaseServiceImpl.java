package com.atguigu.base;

import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Date:2022/5/19
 * Author:zh
 * Description:
 */
public abstract class BaseServiceImpl<T> {
    //用来获得mapper对象
    public abstract BaseMapper<T> getEntityMapper();

    public void insert(T t) {
        getEntityMapper().insert(t);
    }

    public T getById(Long id) {
        return getEntityMapper().getById(id);
    }

    public void delete(Long id) {
        getEntityMapper().delete(id);
    }

    public PageInfo<T> findPage(Map<String, Object> filters) {
        getEntityMapper().findPage(filters);
        //这里需要校验一下pageNum和PageSize的类型,防止输入"a"等类型的导航页
        int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        int pageSize = CastUtil.castInt(filters.get("pageSize"), 10);
        PageHelper.startPage(pageNum, pageSize);
        //navigationpages是底部导航栏的导航数
        return new PageInfo<T>(getEntityMapper().findPage(filters),10);
    }

    public void update(T t) {
        getEntityMapper().update(t);
    }
}
