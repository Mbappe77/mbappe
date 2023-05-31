package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.*;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
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

    @RequestMapping("/info/{id}")
    public Result info(@PathVariable Long id,HttpServletRequest request){
        //调用HouseService中根据id查询房源的方法
        House house = houseService.getById(id);
        //根据房源中小区的id获取所在的小区
        Community community = communityService.getById(house.getCommunityId());
        //获取该房源的经纪人
        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(id);
        //获取房源图片
        List<HouseImage> houseImage1List = houseImageService.findList(id, 1);

        Map<String, Object> map = new HashMap<>();
        map.put("house",house);
        map.put("community",community);
        map.put("houseBrokerList",houseBrokerList);
        map.put("houseImage1List",houseImage1List);
        //从Session域中获取UserInfo对象
        UserInfo userInfo = (UserInfo)request.getSession().getAttribute("USER");
        Boolean isFollow = false;
        if(null != userInfo) {
            //证明已经登录，获取用户的id
            Long userId = userInfo.getId();
            //查询该用户是否已经关注该房源
            isFollow = userFollowService.isFollowed(userId, id);
        }
        //是否关注了该房源
        map.put("isFollow",isFollow);
        return Result.ok(map);
    }

    /*axios.post("/house/list/"+pageNum+"/"+this.page.pageSize)*/
    //http://localhost:8001/house/list/1/2
    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result findListPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @RequestBody HouseQueryVo houseQueryVo){
        PageInfo<HouseVo> pageInfo = houseService.findListPage(pageNum, pageSize, houseQueryVo);
        return Result.ok(pageInfo);
    }
}
