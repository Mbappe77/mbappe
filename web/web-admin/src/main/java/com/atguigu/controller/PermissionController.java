package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Permission;
import com.atguigu.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/permission")
public class PermissionController extends BaseController {

    @Reference
    private PermissionService permissionService;

    private final static String LIST_ACTION = "redirect:/permission";

    private final static String PAGE_INDEX = "permission/index";
    private final static String PAGE_CREATE = "permission/create";
    private final static String PAGE_EDIT = "permission/edit";
    private final static String PAGE_SUCCESS = "common/successPage";

    @RequestMapping
    public String index(ModelMap modelMap){
        List<Permission> list = permissionService.findAllMenu();
        modelMap.addAttribute("list", list);
        return PAGE_INDEX;
    }

    @RequestMapping("/create")
    public String create(ModelMap modelMap,Permission permission){
        modelMap.addAttribute("permission", permission);
        return PAGE_CREATE;
    }

    @RequestMapping("/save")
    public String save(Permission permission){
        permissionService.insert(permission);
        return PAGE_SUCCESS;
    }

    @RequestMapping("/edit/{id}")
    public String update(ModelMap modelMap, @PathVariable Long id){
        Permission permission = permissionService.getById(id);
        modelMap.addAttribute("permission", permission);
        return PAGE_EDIT;
    }

    @RequestMapping("/update")
    public String update(Permission permission){
        permissionService.update(permission);
        return PAGE_SUCCESS;
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        permissionService.delete(id);
        return LIST_ACTION;
    }

}
