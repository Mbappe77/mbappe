package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@SuppressWarnings({"unchecked", "rawtypes"})
public class IndexController {

    @Reference
    private AdminService adminService;

    @Reference
    private PermissionService permissionService;

    private final static String PAGE_INDEX = "frame/index";
    private final static String PAGE_MAIN = "frame/main";
    private final static String PAGE_AUTH     = "frame/auth";

    /**
     * 框架首页
     *
     * @return
     */
    @GetMapping("/")
    public String index(ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        Admin admin = adminService.getByUserName(user.getUsername());
        List<Permission> permissionList = permissionService.findMenuPermissionByAdminId(admin.getId());
        modelMap.addAttribute("admin", admin);
        modelMap.addAttribute("permissionList",permissionList);
        return PAGE_INDEX;
    }

    /**
     * 框架主页
     *
     * @return
     */
    @GetMapping("/main")
    public String main() {

        return PAGE_MAIN;
    }

    @RequestMapping("/login")
    public String goLoginPage(){
        return "frame/login";
    }

    @RequestMapping("/auth")
    public String auth(){
        return PAGE_AUTH;
    }
}
