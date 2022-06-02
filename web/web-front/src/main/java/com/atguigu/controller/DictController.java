package com.atguigu.controller;

/**
 * Date:2022/5/24
 * Author:zh
 * Description:
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController {

    @Reference
    private DictService dictService;

    @GetMapping("/findDictListByParentDictCode/{dictCode}")
    public Result findDictListByParentDictCode(@PathVariable("dictCode") String dictcode) {
        List<Dict> dictList = dictService.findDictListByParentDictCode(dictcode);

        return Result.ok(dictList);
    }

    @GetMapping("/findDictListByParentId/{ParentId}")
    public Result findDictListByParentId(@PathVariable("ParentId") Long id
                                               ) {
        List<Dict> dictList = dictService.findDIctListByParentId(id);

        return Result.ok(dictList);
    }
}
