package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.HouseBroker;
import com.atguigu.mapper.HouseBrokerMapper;
import com.atguigu.mapper.HouseMapper;
import com.atguigu.service.HouseBrokerService;
import com.atguigu.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Date:2022/5/24
 * Author:zh
 * Description:
 */
@Service(interfaceClass = HouseBrokerService.class)
public class HouseBrokerServiceImpl extends BaseServiceImpl<HouseBroker> implements
        HouseBrokerService {
    @Override
    public List<HouseBroker> findAll() {
        return null;
    }

    @Autowired
    private HouseBrokerMapper houseBrokerMapper;

    @Override
    public BaseMapper<HouseBroker> getEntityMapper() {
        return houseBrokerMapper;
    }

    @Override
    public List<HouseBroker> findHouseBrokerListByHouseId(Long houseId) {
        return houseBrokerMapper.findHouseBrokerListbyHouseId(houseId);
    }
}
