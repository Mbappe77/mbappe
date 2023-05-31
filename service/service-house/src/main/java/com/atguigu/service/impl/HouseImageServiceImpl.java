package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.HouseImageDao;
import com.atguigu.entity.HouseImage;
import com.atguigu.service.HouseImageService;
import com.atguigu.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
@Service(interfaceClass = HouseImageService.class)
public class HouseImageServiceImpl extends BaseServiceImpl<HouseImage> implements HouseImageService {
    @Autowired
    private HouseImageDao houseImageDao;
    @Override
    protected BaseDao<HouseImage> getEntityDao() {
        return houseImageDao;
    }

    @Override
    public List<HouseImage> findList(Long houseId, int type) {
        /*HashMap<Object, Object> map = new HashMap<>();
        map.put("houseId",houseId);
        map.put("type",type);*/
        return houseImageDao.findList(houseId,type);
    }
}
