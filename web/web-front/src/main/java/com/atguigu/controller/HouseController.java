package com.atguigu.controller;

import com.atguigu.entity.*;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/house")
public class HouseController {
    
    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private UserFollowService userFollowService;
    
    /**
     * 前端房源分页查询显示
     * @param pageNum
     * @param pageSize
     * @param houseQueryBo
     * @return
     */
    @ResponseBody
    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result list(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @RequestBody HouseQueryBo houseQueryBo){
        PageInfo<HouseVo> pageInfo = houseService.findByPage(pageNum, pageSize, houseQueryBo);
        return Result.ok(pageInfo);
    }

    /**
     * 前端房源详情
     * @param id : 房源id
     * @param session : 会话域
     * @return
     */
    @ResponseBody
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id, HttpSession session){
        //house
        House house = houseService.getById(id);
        //community
        Community community = communityService.getById(house.getCommunityId());
        //houseBrokerList
        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(id);
        //houseImage1List
        List<HouseImage> houseImage1List = houseImageService.findListByTypeAndHouseId(1, id);
        //isFollow : 已登录的账户对当前房源是否关注
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        HashMap<String, Object> map = new HashMap<>();
        if (userInfo == null){
            boolean isFollow = false;
            map.put("isFollow", false);
        }else {
            boolean isFollow = userFollowService.isFollow(userInfo.getId(), id);
            map.put("isFollow", isFollow);
        }
        map.put("house", house);
        map.put("community", community);
        map.put("houseBrokerList", houseBrokerList);
        map.put("houseImage1List", houseImage1List);
        return Result.ok(map);
    }
    
}
