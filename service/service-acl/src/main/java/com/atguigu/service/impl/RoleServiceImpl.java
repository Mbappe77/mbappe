package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.AdminDao;
import com.atguigu.dao.AdminRoleDao;
import com.atguigu.dao.RoleDao;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private AdminRoleDao adminRoleDao;

    @Override
    protected BaseDao<Role> getEntityDao() {
        return roleDao;
    }

    @Override
    public Map<String, Object> findRoleByAdminId(Long adminId) {
        List<Long> exitRoleIdList = adminRoleDao.findRoleIdByAdminId(adminId);
        List<Role> allRoleList = roleDao.findAll();
        List<Role> noAssginRoleList = new ArrayList<>();
        List<Role> assginRoleList = new ArrayList<>();
        for (Role role : allRoleList) {
            if (exitRoleIdList.contains(role.getId())){
                assginRoleList.add(role);
            }else {
                noAssginRoleList.add(role);
            }
        }

        Map<String,Object> map = new HashMap<>();
        map.put("noAssginRoleList",noAssginRoleList);
        map.put("assginRoleList",assginRoleList);
        return map;
    }

    @Override
    public void saveAdminAndRole(Long[] roleIds, Long adminId) {
        adminRoleDao.deleteByAdminId(adminId);

        for (Long roleId : roleIds) {
            if (StringUtils.isEmpty(roleId)) {
                continue;
            }
            AdminRole adminRole = new AdminRole();
            adminRole.setRoleId(roleId);
            adminRole.setAdminId(adminId);
            adminRoleDao.insert(adminRole);
        }
    }
}