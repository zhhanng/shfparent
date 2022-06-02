package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Date:2022/5/22
 * Author:zh
 * Description:
 */
@RestController
@RequestMapping("/dict")
public class DictController extends BaseController {

    @Reference
    private DictService dictService;

    @RequestMapping("/findZnodes")
    public Result findZnodes(@RequestParam(value = "id",defaultValue = "0")Long id){
        List<Map<String, Object>> zones = dictService.findZones(id);
        return Result.ok(zones);
    }


    @GetMapping("/findDictListByParentId/{parentId}")
    @ResponseBody
    public Result findDictListByParentId(@PathVariable("parentId") Long parentId) {
        List<Dict> dictList = dictService.findDIctListByParentId(parentId);
        return Result.ok(dictList);
    }

    /**
     * 根据父节点的dictCode获取子节点数据列表
     * @param dictCode
     * @return
     */
    @GetMapping("/findDictListByParentDictCode/{dictCode}")
    @ResponseBody
    public Result<List<Dict>> findDictListByParentDictCode(@PathVariable String dictCode) {
        List<Dict> list = dictService.findDictListByParentDictCode(dictCode);
        return Result.ok(list);
    }
}
