package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {

    private static final String PAGE_INDEX = "dict/index";

    /**
     * 跳转到dict/index.html页面
     * @return
     */
    @RequestMapping
    public String index(){
        return PAGE_INDEX;
    }

    @Reference
    private DictService dictService;

    /**
     * 获取zTree节点数据
     * @param id : dict的parentId
     * @return
     */
    @ResponseBody
    @GetMapping("/findZnodes")
    public Result findZnodes(@RequestParam(required = true, defaultValue = "0") Long id){
        List<Map<String, Object>> zNodes =  dictService.findZnodesByParentId(id);
        return Result.ok(zNodes);
    }

    /**
     * 根据areaId获取板块列表
     * @param areaId
     * @return
     */
    @ResponseBody
    @GetMapping("/findDictListByParentId/{areaId}")
    public Result findDictListByParentId(@PathVariable Long areaId){
        List<Dict> plateList = dictService.findListByParentId(areaId);
        return Result.ok(plateList);
    }

}
