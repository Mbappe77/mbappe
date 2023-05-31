package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.RolePermission;

import java.util.List;

public interface RolePermissionDao extends BaseDao<RolePermission> {
    List<Long> findPermissionListByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);
}
