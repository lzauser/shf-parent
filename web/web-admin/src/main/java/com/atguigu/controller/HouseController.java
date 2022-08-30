package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.*;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.service.*;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {


    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE = "house/create";
    private static final String PAGE_EDIT = "house/edit";
    private static final String LIST_ACTION = "redirect:/house";
    private static final String PAGE_SHOW = "house/show";
    private static final String PAGE_UPLOAD = "house/upload";

    @Reference
    private CommunityService communityService;
    @Reference
    private DictService dictService;
    @Reference
    private HouseService houseService;

    @Reference
    private HouseImageService houseImageService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private HouseUserService houseUserService;

    /**
     * 跳转'house/index.html'页面
     *
     * @param filters : 搜索条件...
     * @return
     */
    @RequestMapping
    public String index(@RequestParam Map filters, Model model) {
        //数据回显
        model.addAttribute("filters", filters);
        //小区列表communityList
        getListToRequestScope(model);
        //分页查询page
        PageInfo<House> pageInfo = houseService.findPage(filters);
        model.addAttribute("page", pageInfo);
        return PAGE_INDEX;
    }


    /**
     * 获取小区列表，户型列表，楼层列表，建筑结构列表，朝向列表，装修情况列表，房屋用途列表
     * 并存储到request域对象
     *
     * @param model
     */
    private void getListToRequestScope(Model model) {
        List<Community> communityList = communityService.findAll();
        model.addAttribute("communityList", communityList);
        //户型列表houseTypeList
        List<Dict> houseTypeList = dictService.findListByParentCode("houseType");
        model.addAttribute("houseTypeList", houseTypeList);
        //楼层列表floorList
        List<Dict> floorList = dictService.findListByParentCode("floor");
        model.addAttribute("floorList", floorList);
        //建筑结构列表buildStructureList
        List<Dict> buildStructureList = dictService.findListByParentCode("buildStructure");
        model.addAttribute("buildStructureList", buildStructureList);
        //朝向列表directionList
        List<Dict> directionList = dictService.findListByParentCode("direction");
        model.addAttribute("directionList", directionList);
        //装修情况列表decorationList
        List<Dict> decorationList = dictService.findListByParentCode("decoration");
        model.addAttribute("decorationList", decorationList);
        //房屋用途列表houseUseList
        List<Dict> houseUseList = dictService.findListByParentCode("houseUse");
        model.addAttribute("houseUseList", houseUseList);
    }

    @GetMapping("/create")
    public String create(Model model) {
        getListToRequestScope(model);
        return PAGE_CREATE;
    }

    @PostMapping("/save")
    public String save(House house, Model model) {
        houseService.insert(house);
        return successPage(model, "添加房源成功!");
    }


    /**
     * 跳转'house/edit.html'页面
     *
     * @param id : 房源id
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        House house = houseService.getById(id);
        model.addAttribute("house", house);
        getListToRequestScope(model);
        return PAGE_EDIT;
    }


    /**
     * 修改房源
     *
     * @param house
     * @param model
     * @return
     */
    @PostMapping("/update")
    public String update(House house, Model model) {
        houseService.update(house);
        return successPage(model, "修改房源成功!");
    }

    /**
     * 删除房源
     *
     * @param id : 房源id
     * @return
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        houseService.delete(id);
        return LIST_ACTION;
    }


    /**
     * 发布房源
     *
     * @param id     ：房源id
     * @param status : 目标状态
     * @return
     */
    @GetMapping("/publish/{id}/{status}")
    public String publish(@PathVariable Long id, @PathVariable Integer status) {
        houseService.publish(id, status);
        return LIST_ACTION;
    }


    /**
     * /house/show/{id}
     * 跳转'house/show.html'页面
     *
     * @param id : 房源id
     * @return
     */
    @RequestMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        //房源信息house
        House house = houseService.getById(id);
        model.addAttribute("house", house);
        //小区信息community
        Community community = communityService.getById(house.getCommunityId());
        model.addAttribute("community", community);
        //房源图片列表houseImage1List(houseid,type=1)
        List<HouseImage> houseImage1List = houseImageService.findListByTypeAndHouseId(1, id);
        model.addAttribute("houseImage1List", houseImage1List);
        //房产图片列表houseImage2List(houseid,type=2)
        List<HouseImage> houseImage2List = houseImageService.findListByTypeAndHouseId(2, id);
        model.addAttribute("houseImage2List", houseImage2List);
        //经纪人列表houseBrokerList
        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(id);
        model.addAttribute("houseBrokerList", houseBrokerList);
        //房东列表houseUserList
        List<HouseUser> houseUserList = houseUserService.findListByHouseId(id);
        model.addAttribute("houseUserList", houseUserList);
        return PAGE_SHOW;
    }

    /**
     * 跳转house/upload.html页面
     * @param id 用户id
     * @param model
     * @return
     */
    @RequestMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable Long id, Model model){
        model.addAttribute("id", id);
        return PAGE_UPLOAD;
    }

    /**
     * 上传房源封面
     * @param id : 房源id
     * @param file
     * @param model
     * @return
     * @throws IOException
     */
    @PostMapping("/upload/{id}")
    public String upload(@PathVariable Long id, MultipartFile file, Model model) throws IOException {
        //上传文件
        String prefixName = UUID.randomUUID().toString().replace("-", "");
        String suffixName = file.getOriginalFilename().split("\\.")[1];
        String fileName = prefixName + "." + suffixName;
        QiniuUtils.upload2Qiniu(file.getBytes(), fileName);
        //将文件信息保存到hse_house表中
        House house = new House();
        house.setId(id);
        house.setDefaultImageUrl("http://rh2ng10hp.hn-bkt.clouddn.com/" + fileName);
        houseService.update(house);

        return successPage(model, "房源封面上传成功!");
    }

}
