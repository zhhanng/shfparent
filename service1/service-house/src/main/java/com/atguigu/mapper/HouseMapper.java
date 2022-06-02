package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseImage;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.github.pagehelper.Page;

import java.util.List;

public interface HouseMapper extends BaseMapper<House> {
    List<House> findAll();

    /**
     * 创建分页方法
     * @param houseQueryBo
     * @return
     */
    Page<HouseVo> findListPage(HouseQueryBo houseQueryBo);
}
