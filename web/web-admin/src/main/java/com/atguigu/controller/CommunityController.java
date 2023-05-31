package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    private final static String LIST_ACTION = "redirect:/community";

    private final static String PAGE_INDEX = "community/index";
    private final static String PAGE_SHOW = "community/show";
    private final static String PAGE_CREATE = "community/create";
    private final static String PAGE_EDIT = "community/edit";
    private final static String PAGE_SUCCESS = "common/successPage";

    @RequestMapping
    public String index(HttpServletRequest request, ModelMap modelMap){
        Map<String, Object> filters = getFilters(request);
        PageInfo<Community> page = communityService.findPage(filters);
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        if (!filters.containsKey("areaId")){
            filters.put("areaId","");
        }
        if (!filters.containsKey("plateId")){
            filters.put("plateId","");
        }
        modelMap.addAttribute("page",page);
        modelMap.addAttribute("filters",filters);
        modelMap.addAttribute("areaList",areaList);
        return PAGE_INDEX;
    }

    /*opt.openWin('/community/create','新增',630,430)*/
    @RequestMapping("/create")
    public String create(ModelMap modelMap){
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        modelMap.addAttribute("areaList",areaList);
        return PAGE_CREATE;
    }

     /*<form id="ec" action="/community/save" method="post" class="form-horizontal">*/
    @RequestMapping("/save")
    public String save(Community community){
        communityService.insert(community);
        return PAGE_SUCCESS;
    }

    /*opt.openWin('/community/edit/' + id,'修改',580,430);*/
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id,ModelMap modelMap){
        Community community = communityService.getById(id);
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        modelMap.addAttribute("areaList",areaList);
        modelMap.addAttribute("community",community);

        return PAGE_EDIT;
    }

    @RequestMapping("/update")
    public String update(Community community){
        communityService.update(community);
        return PAGE_SUCCESS;
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        communityService.delete(id);
        return LIST_ACTION;
    }
}
