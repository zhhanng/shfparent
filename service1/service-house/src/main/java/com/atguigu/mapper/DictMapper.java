package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Dict;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictMapper extends BaseMapper<Dict> {
    /**
     * 根据父节点查询子节点
     * @return
     */
    List<Dict> findListByParentId(Long id);

    /**
     * 判断是否还有子节点,查询子节点的数量
     * @return
     */
    Integer countIsParent(Long id);

    /**
     * 小区管理
     * @param parentDictCode
     * @return
     */
    List<Dict> findDictListByParentDictCode(String parentDictCode);

 }
