package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController extends BaseController {
    @Reference
    private AdminService adminService;

    @Reference
    private HouseBrokerService houseBrokerService;

    private final static String LIST_ACTION = "redirect:/house/";
    private final static String PAGE_CREATE = "houseBroker/create";
    private final static String PAGE_EDIT = "houseBroker/edit";
    private final static String PAGE_SUCCESS = "common/successPage";

    /*opt.openWin('/houseBroker/create?houseId=[[${house.id}]]','新增经纪人',630,300)*/
    @RequestMapping("/create")
    public String create(@RequestParam("houseId") Long houseId, ModelMap modelMap){
        List<Admin> adminList = adminService.findAll();
        modelMap.addAttribute("houseId", houseId);
        modelMap.addAttribute("adminList", adminList);
        return PAGE_CREATE;
    }

    /*action="/houseBroker/save"*/
    @RequestMapping("/save")
    public String save(HouseBroker houseBroker){
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBrokerService.insert(houseBroker);
        return PAGE_SUCCESS;
    }

    /*opt.openWin('/houseBroker/edit/' + id,'修改经纪人',630,300);*/
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id,ModelMap modelMap){
        HouseBroker houseBroker = houseBrokerService.getById(id);
        List<Admin> adminList = adminService.findAll();
        modelMap.addAttribute("houseBroker",houseBroker);
        modelMap.addAttribute("adminList",adminList);
        return PAGE_EDIT;
    }

    @RequestMapping("/update")
    public String update(HouseBroker houseBroker){
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBrokerService.update(houseBroker);
        return PAGE_SUCCESS;
    }

    /*opt.confirm('/houseBroker/delete/[[${house.id}]]/'+id);*/
    @RequestMapping("/delete/{houseId}/{brokerId}")
    public String delete(@PathVariable("houseId") Long houseId,@PathVariable("brokerId") Long brokerId){
        houseBrokerService.delete(brokerId);
        return LIST_ACTION+houseId;
    }
}
