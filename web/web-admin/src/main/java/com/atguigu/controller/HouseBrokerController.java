package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/houseBroker") 
public class HouseBrokerController extends BaseController {

    private static final String PAGE_CREATE = "houseBroker/create";
    private static final String PAGE_EDIT = "houseBroker/edit";
    private static final String SHOW_ACTION = "redirect:/house/";

    @Reference
    private AdminService adminService;
    @Reference
    private HouseBrokerService houseBrokerService;

    /**
     * 添加经纪人
     * @param houseBroker : 经纪人
     * @param model 
     * @return
     */
    @RequestMapping("/create")
    public String create(HouseBroker houseBroker, Model model){
        model.addAttribute("houseBroker", houseBroker);
        //查询用户列表adminList
        List<Admin> adminList = adminService.findAll();
        model.addAttribute("adminList", adminList);
        return PAGE_CREATE;
    }

    /**
     * 添加经纪人保存
     * @param houseBroker
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(HouseBroker houseBroker, Model model){
        Long brokerId = houseBroker.getBrokerId();
        Admin admin = adminService.getById(brokerId);
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBrokerService.insert(houseBroker);
        return successPage(model, "添加经纪人成功~");
    }

    /**
     * 跳转'houseBroker/edit.html'页面
     *
     * @param id : hse_house_broker的id字段
     * @return
     */
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        HouseBroker houseBroker = houseBrokerService.getById(id);
        model.addAttribute("houseBroker", houseBroker);
        List<Admin> adminList = adminService.findAll();
        model.addAttribute("adminList", adminList);
        return PAGE_EDIT;
    }

    /**
     * @param id : 当前经纪人的主键值(id)
     * @param brokerId : 新经纪人的broker_id (admin的id)
     * @return
     */
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, Long brokerId, Model model){
        HouseBroker houseBroker = new HouseBroker();
        //规定要修改哪一个houseBroker
        houseBroker.setId(id);
        //规定修改的内容
        Admin admin = adminService.getById(brokerId);
        houseBroker.setBrokerId(brokerId);
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBrokerService.update(houseBroker);
        return successPage(model, "修改经纪人成功~");
    }

    /**
     * @param houseId : 房源id
     * @param id : hse_house_broker的id
     * @return
     */
    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable Long houseId, @PathVariable Long id) {
        houseBrokerService.delete(id);
        //删除成功后， 重定向到'house/show.html'页面
        return SHOW_ACTION + houseId;
    }
    
}
