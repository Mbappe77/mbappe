package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.DictDao;
import com.atguigu.dao.HouseDao;
import com.atguigu.entity.Community;
import com.atguigu.entity.House;
import com.atguigu.service.HouseService;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

@Service(interfaceClass = HouseService.class)
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {

    @Autowired
    private HouseDao houseDao;

    @Autowired
    private DictDao dictDao;

    @Override
    protected BaseDao<House> getEntityDao() {
        return houseDao;
    }

    @Override
    public void publish(Long id, Integer status) {
        House house = new House();
        house.setId(id);
        house.setStatus(status);
        houseDao.update(house);
    }

    @Override
    public PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo) {
        PageHelper.startPage(pageNum,pageSize);
        Page<HouseVo> page = houseDao.findListPage(houseQueryVo);
        List<HouseVo> result = page.getResult();
        for (HouseVo houseVo : result) {
            String houseTypeName = dictDao.getNameById(houseVo.getHouseTypeId());
            String floorName = dictDao.getNameById(houseVo.getFloorId());
            String directionName = dictDao.getNameById(houseVo.getDirectionId());
            houseVo.setHouseTypeName(houseTypeName);
            houseVo.setFloorName(floorName);
            houseVo.setDirectionName(directionName);
        }
        return new PageInfo<HouseVo>(page,10);
    }

    @Override
    public House getById(Serializable id) {
        House house = houseDao.getById(id);
        if (house == null) {
            return null;
        }
        house.setFloorName(dictDao.getNameById(house.getFloorId()));
        house.setBuildStructureName(dictDao.getNameById(house.getBuildStructureId()));
        house.setDirectionName(dictDao.getNameById(house.getDirectionId()));
        house.setDecorationName(dictDao.getNameById(house.getDecorationId()));
        house.setHouseUseName(dictDao.getNameById(house.getHouseUseId()));

        return house;
    }
}
