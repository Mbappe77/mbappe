package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Role;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value="/admin")
@SuppressWarnings({"unchecked", "rawtypes"})
public class AdminController extends BaseController {

    @Reference
    private AdminService adminService;

    @Reference
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final static String LIST_ACTION = "redirect:/admin";

    private final static String PAGE_INDEX = "admin/index";
    private final static String PAGE_CREATE = "admin/create";
    private final static String PAGE_EDIT = "admin/edit";
    private final static String PAGE_SUCCESS = "common/successPage";
    private final static String PAGE_UPLOED_SHOW = "admin/upload";
    private final static String PAGE_ASSIGN_SHOW = "admin/assignShow";


//     <form id="ec" th:action="@{/admin/assignRole}"
    @RequestMapping("/assignRole")
    public String assignRole(Long[] roleIds,Long adminId){
        roleService.saveAdminAndRole(roleIds,adminId);
        return PAGE_SUCCESS;
    }

    //    opt.openWin('/admin/assignShow/'+id,'分配角色',550,450)
    @RequestMapping("/assignShow/{id}")
    public String assignShow(@PathVariable Long id,ModelMap modelMap){
        Map<String,Object> roleMap = roleService.findRoleByAdminId(id);
        modelMap.addAllAttributes(roleMap);
        modelMap.addAttribute("adminId",id);
        return PAGE_ASSIGN_SHOW;
    }


    @RequestMapping
    public String index(ModelMap model, HttpServletRequest request) {
        Map<String,Object> filters = getFilters(request);
        PageInfo<Admin> page = adminService.findPage(filters);

        model.addAttribute("page", page);
        model.addAttribute("filters", filters);
        return PAGE_INDEX;
    }

    @GetMapping("/create")
    public String create() {
        return PAGE_CREATE;
    }

    /*@PostMapping("/save")
    public String save(Admin admin) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        admin.setPassword(encoder.encode(admin.getPassword()));
        //设置默认头像
        admin.setHeadUrl("http://139.198.127.41:9000/sph/20230505/default_handsome.jpg");
        adminService.insert(admin);

        return PAGE_SUCCESS;
    }*/
    @RequestMapping("/save")
    public String save(Admin admin){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        admin.setPassword(encoder.encode(admin.getPassword()));
        // 设置头像 ，没有头像服务器，设置默认值
        admin.setHeadUrl("http://139.198.127.41:9000/sph/20230505/default_handsome.jpg");
        adminService.insert(admin);
        return PAGE_SUCCESS;
    }


    @GetMapping("/edit/{id}")
    public String edit(ModelMap model,@PathVariable Long id) {
        Admin admin = adminService.getById(id);
        model.addAttribute("admin",admin);
        return PAGE_EDIT;
    }


    @PostMapping(value="/update")
    public String update(Admin admin) {

        adminService.update(admin);

        return PAGE_SUCCESS;
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        adminService.delete(id);
        return LIST_ACTION;
    }

    /*opt.openWin('/admin/uploadShow/'+id,'上传头像',580,300);*/
    @RequestMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable Long id,ModelMap modelMap){
        modelMap.addAttribute("id",id);
        return PAGE_UPLOED_SHOW;
    }

    /*th:action="@{/admin/upload/{id}(id=${id})}"*/
    @RequestMapping("/upload/{id}")
    public String upload(@PathVariable Long id, @RequestParam(value = "file")MultipartFile file,HttpServletRequest request) throws Exception {
        try {
            String newFileName =  UUID.randomUUID().toString() ;
            // 上传图片
            QiniuUtils.upload2Qiniu(file.getBytes(),newFileName);
            String url= "http://rv51gqdiz.hn-bkt.clouddn.com/"+ newFileName;
            Admin admin = new Admin();
            admin.setId(id);
            admin.setHeadUrl(url);
            adminService.update(admin);
            return PAGE_SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}