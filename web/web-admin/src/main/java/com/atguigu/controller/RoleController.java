package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping(value="/role")
public class RoleController extends BaseController {

    @Reference
    private RoleService roleService;

    @Reference
    private PermissionService permissionService;

    private final static String LIST_ACTION = "redirect:/role";
    private final static String PAGE_INDEX = "role/index";
    private final static String PAGE_CREATE = "role/create";
    private final static String PAGE_EDIT = "role/edit";
    private final static String PAGE_SUCCESS = "common/successPage";
    private final static String PAGE_ASSGIN_SHOW= "role/assginShow";

    /*
    * 保存权限
    *  <form id="ec" th:action="@{/role/assignPermission}"
    * */
    @RequestMapping("/assignPermission")
    public String assignPermission(Long roleId,Long[] permissionIds){

        permissionService.saveRoleIdAndPermissionIds(roleId,permissionIds);

        return PAGE_SUCCESS;

    }

    @PreAuthorize("hasAnyAuthority('role.show')")
    @RequestMapping
    public String index(ModelMap model, HttpServletRequest request) {
        Map<String,Object> filters = getFilters(request);
        PageInfo<Role> page = roleService.findPage(filters);

        model.addAttribute("page", page);
        model.addAttribute("filters", filters);
        return PAGE_INDEX;
    }

    @PreAuthorize("hasAnyAuthority('role.create')")
    @RequestMapping("/create")
    public String create(ModelMap model) {
        return PAGE_CREATE;
    }

    @PreAuthorize("hasAnyAuthority('role.create2')")
    @RequestMapping("/save")
    public String save(Role role) {
        roleService.insert(role);
        return PAGE_SUCCESS;
    }
    @PreAuthorize("hasAnyAuthority('role.edit')")
    @RequestMapping("/edit/{id}")
    public String edit(ModelMap model,@PathVariable Long id) {
        Role role = roleService.getById(id);
        model.addAttribute("role",role);
        return PAGE_EDIT;
    }

    @RequestMapping(value="/update")
    public String update(Role role) {

        roleService.update(role);
        return PAGE_SUCCESS;
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        roleService.delete(id);
        return LIST_ACTION;
    }


//    给角色分配权限
//    opt.openWin("/role/assignShow/"+id,'修改',580,430);
    @RequestMapping("/assignShow/{roleId}")
    public String assignShow(@PathVariable Long roleId,ModelMap modelMap){
        List<Map<String,Object>> zNodes = permissionService.findPermissionByRoleId(roleId);
        modelMap.put("zNodes", zNodes);
        modelMap.put("roleId", roleId);
        return PAGE_ASSGIN_SHOW;
    }
}