package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Dict;

import java.util.List;
import java.util.Map;

public interface DictService {
    List<Map<String,Object>> findZones(Long parentId);

    List<Dict> findDictListByParentDictCode(String parentDictCode);
    /**
     * 根据父节点的id查询其所有的子节点
     * @param parentId
     * @return
     */
    List<Dict> findDIctListByParentId(Long parentId);
}
