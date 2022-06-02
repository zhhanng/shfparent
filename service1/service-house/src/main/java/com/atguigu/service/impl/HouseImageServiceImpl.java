package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseImage;
import com.atguigu.mapper.HouseImageMapper;
import com.atguigu.service.HouseImageService;
import com.atguigu.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Date:2022/5/24
 * Author:zh
 * Description:
 */
@Service(interfaceClass = HouseImageService.class)
public class HouseImageServiceImpl extends BaseServiceImpl<HouseImage> implements HouseImageService {

   @Autowired
   private HouseImageMapper houseImageMapper;

    @Override
    public BaseMapper getEntityMapper() {
        return houseImageMapper;
    }

    @Override
    public List<HouseImage> findAll() {
        return null;
    }

    @Override
    public List<HouseImage> findHouseImageList(Long houseId, Long type) {
        return houseImageMapper.findHouseImageList(houseId, type);
    }
}
