package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseImageMapper extends BaseMapper<HouseImage> {
    List<HouseImage> findHouseImageList(@Param("houseId")Long HouseId,@Param("type") Long type);
}
