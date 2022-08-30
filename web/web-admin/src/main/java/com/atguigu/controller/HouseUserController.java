package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.HouseUser;
import com.atguigu.service.HouseUserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/houseUser")
public class HouseUserController extends BaseController {


    private static final String PAGE_CREATE = "houseUser/create";
    private static final String PAGE_EDIT = "houseUser/edit";
    private static final String SHOW_ACTION = "redirect:/house/";

    @Reference
    private HouseUserService houseUserService;

    /**
     * 跳转'house/create.html'页面
     *
     * @param houseUser : houseId
     * @return
     */
    @RequestMapping("/create")
    public String create(HouseUser houseUser, Model model) {
        model.addAttribute("houseUser", houseUser);
        return PAGE_CREATE;

    }

    @PostMapping("/save")
    public String save(HouseUser houseUser, Model model) {

        houseUserService.insert(houseUser);

        return successPage(model, "添加房东成功!");
    }


    /**
     * 跳转'houseUser/edit.html'页面
     *
     * @param id : 房东id
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        HouseUser houseUser = houseUserService.getById(id);
        model.addAttribute("houseUser", houseUser);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(HouseUser houseUser, Model model) {
        houseUserService.update(houseUser);
        return successPage(model, "修改房东成功!");
    }

    /**
     * 删除房东功能
     *
     * @param houseId : 房源id
     * @param id      : 房东id
     * @return
     */
    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable Long houseId, @PathVariable Long id) {
        houseUserService.delete(id);
        return SHOW_ACTION + houseId;
    }


}
