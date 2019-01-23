package com.lzj.cache.service;

import com.lzj.cache.bean.Department;
import com.lzj.cache.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

@Service
public class DeptService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private RedisCacheManager redisCacheManager;

    //注解的方式
    //获取部门信息
   /* @Cacheable(cacheNames = {"dept"}, key="#id", unless="#result == null")
    public Department getDeptById(Integer id) {
        Department department = departmentMapper.getDepartmentById(id);
        return department;
    }*/


   //编码的方式
    public Department getDeptById(Integer id) {
        Department department = departmentMapper.getDepartmentById(id);

        //采用编码的方式存入缓存
        Cache dept = redisCacheManager.getCache("dept");
        dept.put("dept:111", department);

        return department;
    }
}
