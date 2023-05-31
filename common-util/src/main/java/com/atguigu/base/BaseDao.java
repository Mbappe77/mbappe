package com.atguigu.base;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import javax.management.relation.Role;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDao<T> {

    List<T> findAll();
    void insert(T t);

    T getById(Serializable id);

    void update(T t);

    void delete(Serializable id);

    Page<T> findPage(Map<String, Object> filters);
}