package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.entity.Dict;
import com.atguigu.mapper.DictMapper;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date:2022/5/21
 * Author:zh
 * Description:
 */
@Service(interfaceClass = DictService.class)
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public List<Map<String, Object>> findZones(Long parentId) {
        List<Dict> dictList = dictMapper.findListByParentId(parentId);
        List<Map<String, Object>> znodes = new ArrayList<>();
        for (Dict dict : dictList) {
            HashMap<String, Object> znode = new HashMap<>();
            znode.put("id", dict.getId());
            znode.put("name", dict.getName());
            znode.put("isParent", dictMapper.countIsParent(dict.getId()) > 0);
            znodes.add(znode);
        }
        return znodes;
    }

    @Override
    public List<Dict> findDictListByParentDictCode(String parentDictCode) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //根据key找value
            String value = jedis.get("shf:dict:parentDictId:" + parentDictCode);
            //第一次查询的时候可能找不到,此时需要判断,不为null,说明从
            //redis中找到了value,value是jason数据形式
            if (!StringUtils.isEmpty(value)) {
                return JSON.parseArray(value, Dict.class);
            }
            //到这说明未在redis中找到,去mysql数据库中查询
            List<Dict> dictList = dictMapper.findDictListByParentDictCode(parentDictCode);

            //还要在放入redis数据库中,redis中放入的数据是JSON格式的
            jedis.set("shf:dict:parentDictId:" + parentDictCode, JSON.toJSONString(dictList));
            return dictList;
        } finally {
            if (jedis == null) {
                jedis.close();
                if (jedis.isConnected()) {
                    jedis.disconnect();
                }
            }
        }

    }

    @Override
    public List<Dict> findDIctListByParentId(Long parentId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //先从redis里边找,如果没找到再去mysql里边找
            String value = jedis.get("shf:dict:parentId"+parentId);
            if (!
                    StringUtils.isEmpty(value)) {
                //redis里边有,就直接返回,JSON数据解析成所需求的数组返回
                return JSON.parseArray(value, Dict.class);
            }
            //redis里边没有,需要去Mysql里边查找并存入redis中
            List<Dict> dictList = dictMapper.findListByParentId(parentId);
            //存入redis中,redis需要JSON数据
            jedis.set("shf:dict:parentId"+parentId, JSON.toJSONString(dictList));
            return dictList;
        } finally {
            //finally里边需要归还和关闭jedis链接
            if (jedis!=null){
                jedis.close();
                if (jedis.isConnected()) {
                    jedis.disconnect();
                }
            }
        }
    }
}
