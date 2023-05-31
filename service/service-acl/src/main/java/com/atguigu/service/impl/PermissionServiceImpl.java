package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.PermissionDao;
import com.atguigu.dao.RolePermissionDao;
import com.atguigu.entity.Permission;
import com.atguigu.entity.RolePermission;
import com.atguigu.helper.PermissionHelper;
import com.atguigu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    protected BaseDao<Permission> getEntityDao() {
        return permissionDao;
    }

    @Override
    public List<Map<String, Object>> findPermissionByRoleId(Long roleId) {

        List<Long> permissionList = rolePermissionDao.findPermissionListByRoleId(roleId);
        List<Permission> permissionAllList = permissionDao.findAll();
        List<Map<String,Object>> zNodes = new ArrayList<>();
        for (Permission permission : permissionAllList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id",permission.getId());
            map.put("pId",permission.getParentId());
            map.put("name",permission.getName());
            if (permissionList.contains(permission.getId())) {
                map.put("checked",true);
            }
            zNodes.add(map);
        }
        return zNodes;
    }

    @Override
    public void saveRoleIdAndPermissionIds(Long roleId, Long[] permissionIds) {

        rolePermissionDao.deleteByRoleId(roleId);
        for (Long permissionId : permissionIds) {
            if (StringUtils.isEmpty(permissionId)) {
                continue;
            }
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionDao.insert(rolePermission);
        }

    }

    @Override
    public List<Permission> findMenuPermissionByAdminId(Long adminId) {
        List<Permission> permissionList = null;

        if (adminId.longValue() == 1){
            permissionList = permissionDao.findAll();
        }else {
            permissionList = permissionDao.findListByAdminId(adminId);
        }
        List<Permission> result = PermissionHelper.bulid(permissionList);

        return result;
    }

    @Override
    public List<Permission> findAllMenu() {
        List<Permission> permissionList = permissionDao.findAll();
        if (CollectionUtils.isEmpty(permissionList)){
            return null;
        }
        List<Permission> result = PermissionHelper.bulid(permissionList);
        return result;
    }

    @Override
    public List<String> findCodeListByAdminId(Long adminId) {
        //超级管理员admin账号id为：1 //证明是系统管理员，查询所有权限
        if(adminId.longValue() == 1) {
            return permissionDao.findAllCodeList();
        }
        //根据管理员的id查询操作权限
        return permissionDao.findCodeListByAdminId(adminId);
    }
}
