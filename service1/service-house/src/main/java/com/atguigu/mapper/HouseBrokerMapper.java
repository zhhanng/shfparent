package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseBroker;

import java.util.List;

public interface HouseBrokerMapper extends BaseMapper<HouseBroker> {
    List<HouseBroker> findHouseBrokerListbyHouseId(Long houseId);
}
