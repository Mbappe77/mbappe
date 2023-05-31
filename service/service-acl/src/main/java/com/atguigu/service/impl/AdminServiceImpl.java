package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.AdminDao;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;


@Service(interfaceClass = AdminService.class)
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService {
@Autowired
private AdminDao adminDao;
    @Override
    protected BaseDao<Admin> getEntityDao() {
        return adminDao;
    }

    @Override
    public Admin getByUserName(String userName) {
        return adminDao.getByUserName(userName);
    }
}
