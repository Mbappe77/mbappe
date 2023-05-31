package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.service.UserFollowService;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.plugin.Intercepts;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/userFollow")
public class UserFollowController extends BaseController {

    @Reference
    private UserFollowService userFollowService;


    @RequestMapping("/auth/follow/{houseId}")
    public Result follow(@PathVariable("houseId") Long houseId, HttpServletRequest request){
        UserInfo userInfo =(UserInfo) request.getSession().getAttribute("USER");
        Long userInfoId = userInfo.getId();
        userFollowService.follow(userInfoId,houseId);
        return Result.ok();
    }

    //查询我的关注
    @RequestMapping(value = "/auth/list/{pageNum}/{pageSize}")
    public Result findListPage(@PathVariable Integer pageNum,
                               @PathVariable Integer pageSize,
                               HttpServletRequest request) {
        //获取Session域中中的UserInfo对象
        UserInfo userInfo = (UserInfo)request.getSession().getAttribute("USER");
        Long userInfoId = userInfo.getId();
        //调用UserFollowService中查询我的关注的方法
        PageInfo<UserFollowVo> pageInfo = userFollowService.findListPage(pageNum, pageSize, userInfoId);
        return Result.ok(pageInfo);
    }

     /*axios.get('/userFollow/auth/cancelFollow/'+id)*/
    @RequestMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable Long id){
        userFollowService.delete(id);
        return Result.ok();
    }
}
