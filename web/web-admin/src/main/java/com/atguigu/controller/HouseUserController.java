package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.HouseUser;
import com.atguigu.service.HouseUserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/houseUser")
public class HouseUserController extends BaseController {

    @Reference
    private HouseUserService houseUserService;

    private final static String LIST_ACTION = "redirect:/house/";

    private final static String PAGE_CREATE = "houseUser/create";
    private final static String PAGE_EDIT = "houseUser/edit";
    private final static String PAGE_SUCCESS = "common/successPage";

     /*opt.openWin('/houseUser/create?houseId=[[${house.id}]]','新增房东',630,430)*/
    @RequestMapping("/create")
    public String create(ModelMap modelMap, @RequestParam("houseId") Long houseId){
        modelMap.addAttribute("houseId", houseId);
        return PAGE_CREATE;
    }

    /*action="/houseUser/save"*/
    @RequestMapping("/save")
    public String save(HouseUser houseUser){
        houseUserService.insert(houseUser);
        return PAGE_SUCCESS;
    }

    /*opt.openWin('/houseUser/edit/' + id,'修改房东',630,430);*/
    @RequestMapping("/edit/{id}")
    public String edit(ModelMap modelMap, @PathVariable Long id){
        HouseUser houseUser = houseUserService.getById(id);
        modelMap.addAttribute("houseUser", houseUser);
        return PAGE_EDIT;
    }

    @RequestMapping("update")
    public String update(HouseUser houseUser){
        houseUserService.update(houseUser);
        return PAGE_SUCCESS;
    }

    @RequestMapping("delete/{houseId}/{id}")
    public String delete(ModelMap modelMap,@PathVariable Long houseId,@PathVariable Long id){
        houseUserService.delete(id);
        return LIST_ACTION+houseId;
    }
}
