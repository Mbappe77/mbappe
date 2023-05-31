package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/houseImage")
public class HouseImageController extends BaseController {
    @Reference
    private HouseImageService houseImageService;

    private final static String LIST_ACTION = "redirect:/house/";
    private final static String PAGE_UPLOED_SHOW = "house/upload";

    /*opt.openWin('/houseImage/uploadShow/[[${house.id}]]/1','上传房源图片',580,430);*/
    @RequestMapping("/uploadShow/{houseId}/{type}")
    public String uploadShow(ModelMap modelMap, @PathVariable Long houseId,@PathVariable Integer type){
        modelMap.addAttribute("houseId",houseId);
        modelMap.addAttribute("type",type);
        return PAGE_UPLOED_SHOW;
    }

    /*opt.openWin('/houseImage/uploadShow/[[${house.id}]]/2','上传房产图片',580,430);*/
    @RequestMapping("/upload/{houseId}/{type}")
    @ResponseBody
    public Result upload(@PathVariable Long houseId,@PathVariable Integer type,@RequestParam(value = "file") MultipartFile[] files) throws Exception{
        if (files.length>0){
            for (MultipartFile file : files){
                String newFileName = UUID.randomUUID().toString();
                QiniuUtils.upload2Qiniu(file.getBytes(),newFileName);
                String url = "http://rv51gqdiz.hn-bkt.clouddn.com/"+newFileName;

                HouseImage houseImage = new HouseImage();
                houseImage.setHouseId(houseId);
                houseImage.setType(type);
                houseImage.setImageName(file.getOriginalFilename());
                houseImage.setImageUrl(url);
                houseImageService.insert(houseImage);
            }
        }
        return Result.ok();
    }

    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(ModelMap modelMap, @PathVariable Long houseId, @PathVariable Long id, RedirectAttributes redirectAttributes){
        HouseImage houseImage = houseImageService.getById(id);
        houseImageService.delete(id);
        QiniuUtils.deleteFileFromQiniu(houseImage.getImageUrl());
        return LIST_ACTION + houseId;
    }
}
