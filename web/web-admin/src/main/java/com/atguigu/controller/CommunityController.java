package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {

    private static final String PAGE_INDEX = "community/index";
    private static final String PAGE_CREATE = "community/create";
    private static final String PAGE_EDIT = "community/edit";
    private static final String LIST_ACTION = "redirect:/community";

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    /**
     * 小区管理首页分页查询
     * @param filters
     * @param model
     * @return
     */
    @RequestMapping
    public String index(@RequestParam Map<String, Object> filters, Model model){
        if (!filters.containsKey("areaId")){
            filters.put("areaId", "");
        }
        if (!filters.containsKey("plateId")){
            filters.put("plateId", "");
        }
        model.addAttribute("filters", filters);

        //根据北京id获取区域列表
        List<Dict> areaList = dictService.findListByParentId(110000L);
        model.addAttribute("areaList", areaList);

        //重写之后的分页查询
        PageInfo<Community> pageInfo = communityService.findPage(filters);
        model.addAttribute("page", pageInfo);
        return PAGE_INDEX;
    }

    /**
     * 查询区域和板块数据,返回给前端页面,跳转到community/create.html页面
     * @param model
     * @return
     */
    @RequestMapping("/create")
    public String create(Model model){
        List<Dict> areaList = dictService.findListByParentId(110000L);
        model.addAttribute("areaList", areaList);
        return PAGE_CREATE;
    }

    /**
     * 添加小区
     * @param community
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(Community community, Model model){
        communityService.insert(community);
        return successPage(model, "添加小区成功~");
    }

    /**
     * 查询小区信息返回给修改小区页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        Community community = communityService.getById(id);
        model.addAttribute("community", community);
        List<Dict> areaList = dictService.findListByParentId(110000L);
        model.addAttribute("areaList", areaList);
        return PAGE_EDIT;
    }

    /**
     * 修改小区信息
     * @param community
     * @param model
     * @return
     */
    @PostMapping("/update")
    public String update(Community community, Model model){
        communityService.update(community);
        return successPage(model, "修改小区成功~");
    }

    /**
     * 删除小区
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        communityService.delete(id);
        return LIST_ACTION;
    }

}
