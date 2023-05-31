package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController extends BaseController {
    @Reference
    private DictService dictService;


    @RequestMapping("/findListByParentId/{id}")
    public Result<List<Dict>> findListByParentId(@PathVariable Long id){
        List<Dict> list = dictService.findListByParentId(id);
        return Result.ok(list);

    }

    @RequestMapping("/findListByDictCode/{dictCode}")
    @ResponseBody
    public Result<List<Dict>> findListByDictCode(@PathVariable String dictCode){
        List<Dict> list = dictService.findListByDictCode(dictCode);
        return Result.ok(list);
    }
}
