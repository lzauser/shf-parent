package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {
    
    @Reference
    private DictService dictService;

    /**
     * 根据数据字典父编码查询数据字典列表
     * @param dictCode : 父编码
     * @return
     */
    @ResponseBody
    @RequestMapping("/findDictListByParentDictCode/{dictCode}")
    public Result findDictListByParentDictCode(@PathVariable String dictCode){
        List<Dict> dictList = dictService.findListByParentCode(dictCode);
        return Result.ok(dictList);
    }

    /**
     * 根据数据字典父id查询数据字典列表
     * @param id : 父id
     * @return
     */
    @ResponseBody
    @GetMapping("/findDictListByParentId/{id}")
    public Result findDictListByParentId(@PathVariable Long id) {
        List<Dict> plateList = dictService.findListByParentId(id);
        return Result.ok(plateList);
    }
}
