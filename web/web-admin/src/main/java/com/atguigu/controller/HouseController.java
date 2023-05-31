package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.House;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {
    @Reference
    private HouseService houseService;

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    @Reference
    private HouseImageService houseImageService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private HouseUserService houseUserService;

    private final static String LIST_ACTION = "redirect:/house";

    private final static String PAGE_INDEX = "house/index";
    private final static String PAGE_SHOW = "house/show";
    private final static String PAGE_CREATE = "house/create";
    private final static String PAGE_EDIT = "house/edit";
    private final static String PAGE_SUCCESS = "common/successPage";

    @RequestMapping
    public String index(HttpServletRequest request, ModelMap modelMap) {
        Map<String, Object> filters = getFilters(request);
        PageInfo<House> page = houseService.findPage(filters);
        modelMap.addAttribute("page", page);
        modelMap.addAttribute("filters", filters);
        modelMap.addAttribute("communityList", communityService.findAll());

        modelMap.addAttribute("houseTypeList", dictService.findListByDictCode("houseType"));

        modelMap.addAttribute("floorList",dictService.findListByDictCode("floor"));

        modelMap.addAttribute("buildStructureList", dictService.findListByDictCode("buildStructure"));

        modelMap.addAttribute("directionList", dictService.findListByDictCode("direction"));

        modelMap.addAttribute("decorationList", dictService.findListByDictCode("decoration"));

        modelMap.addAttribute("houseUseList", dictService.findListByDictCode("houseUse"));

        return PAGE_INDEX;
    }

    /*opt.openWin('/house/create','新增',630,430)*/
    @RequestMapping("/create")
    public String create(ModelMap modelMap){
        modelMap.addAttribute("communityList",communityService.findAll());
        modelMap.addAttribute("houseTypeList",dictService.findListByDictCode("houseType"));
        modelMap.addAttribute("floorList",dictService.findListByDictCode("floor"));
        modelMap.addAttribute("buildStructureList",dictService.findListByDictCode("buildStructure"));
        modelMap.addAttribute("directionList",dictService.findListByDictCode("direction"));
        modelMap.addAttribute("decorationList",dictService.findListByDictCode("decoration"));
        modelMap.addAttribute("houseUseList",dictService.findListByDictCode("houseUse"));
        return PAGE_CREATE;
    }

     /*<form id="ec" th:action="@{/house/save}" method="post" class="form-horizontal">*/
    @RequestMapping("/save")
    public String save(House house){
        house.setStatus(0);
        houseService.insert(house);
        return PAGE_SUCCESS;
    }

     /*opt.openWin('/house/edit/' + id,'修改',630,430);*/
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id,ModelMap modelMap){
        House house = houseService.getById(id);
        modelMap.addAttribute("house",house);
        modelMap.addAttribute("communityList",communityService.findAll());
        modelMap.addAttribute("houseTypeList",dictService.findListByDictCode("houseType"));
        modelMap.addAttribute("floorList",dictService.findListByDictCode("floor"));
        modelMap.addAttribute("buildStructureList",dictService.findListByDictCode("buildStructure"));
        modelMap.addAttribute("directionList",dictService.findListByDictCode("direction"));
        modelMap.addAttribute("decorationList",dictService.findListByDictCode("decoration"));
        modelMap.addAttribute("houseUseList",dictService.findListByDictCode("houseUse"));
        return PAGE_EDIT;
    }

    @RequestMapping("/update")
    public String update(House house){
        houseService.update(house);
        return PAGE_SUCCESS;
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        houseService.delete(id);
        return LIST_ACTION;
    }

    /*opt.confirm("/house/publish/" + id + "/" + status, "确定该操作吗？");*/
    @RequestMapping("/publish/{id}/{status}")
    public String publish(@PathVariable Long id,@PathVariable Integer status){
        houseService.publish(id,status);
        return LIST_ACTION;
    }

    /*window.location = "/house/" + id;*/
    @RequestMapping("/{id}")
    public String show(@PathVariable Long id,ModelMap modelMap){
        House house = houseService.getById(id);
        Community community = communityService.getById(house.getCommunityId());

        modelMap.addAttribute("house",house);
        modelMap.addAttribute("community",community);
        modelMap.addAttribute("houseImage1List",houseImageService.findList(id,1));
        modelMap.addAttribute("houseImage2List",houseImageService.findList(id,2));
        modelMap.addAttribute("houseBrokerList",houseBrokerService.findListByHouseId(id));
        modelMap.addAttribute("houseUserList",houseUserService.findListByHouseId(id));

        return PAGE_SHOW;
    }
}
