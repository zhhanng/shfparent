package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.House;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.mapper.HouseMapper;
import com.atguigu.service.HouseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Date:2022/5/23
 * Author:zh
 * Description:
 */
@Service(interfaceClass = HouseService.class)
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {

    @Autowired
    private HouseMapper houseMapper;

    @Override
    public BaseMapper<House> getEntityMapper() {
        return houseMapper;
    }

    @Override
    public List<House> findAll() {
        return houseMapper.findAll();
    }

    @Override
    public void publish(Long id, Integer status) {
        House house = new House();
        house.setStatus(status);
        house.setId(id);
        houseMapper.update(house);
    }

    @Override
    public PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryBo houseQueryBo) {
        PageHelper.startPage(pageNum,pageSize);
       Page<HouseVo> page=houseMapper.findListPage(houseQueryBo);
       return new PageInfo<HouseVo>(page);
    }
}
