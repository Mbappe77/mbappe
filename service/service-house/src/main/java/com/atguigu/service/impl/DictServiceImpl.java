package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Dict;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = DictService.class)
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService {

    @Autowired
    private DictDao dictDao;

    @Override
    protected BaseDao<Dict> getEntityDao() {
        return dictDao;
    }

    /**
     * 查询数据库
     * @param id
     * @return
     */
    @Override
    public List<Map<String, Object>> findZnodes(Long id) {
        // 根据0，查询所有的1,根据1，找到10000
        List<Dict> list =  dictDao.findListByParentId(id);


        // 需要把数据库里面的数据变成树
//        [{ id:'011', name:'n1.n1', isParent:true}]
        List<Map<String, Object>> data = new ArrayList<>();
        for (Dict dict : list) {
            // 查询有多少个count
           Integer count =  dictDao.countIsParent(dict.getId());
           Map<String, Object> map = new HashMap<>();

           map.put("id",dict.getId());
           map.put("name",dict.getName());
           map.put("isParent",count>0?true:false);
           data.add(map);
        }
        return data;
    }

    @Override
    public List<Dict> findListByParentId(Long parentId) {
        return dictDao.findListByParentId(parentId);
    }

    @Override
    public List<Dict> findListByDictCode(String dictCode) {
        Dict dict = dictDao.getByDictCode(dictCode);
        if (dict == null) {
            return null;
        }
        return this.findListByParentId(dict.getId());
    }

    @Override
    public String getNameById(Long Id) {
        return dictDao.getNameById(Id);
    }
}
