package com.lzj.cache.mapper;

import com.lzj.cache.bean.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    @Select("SELECT * FROM department WHERE id = #{id}")
    Department getDepartmentById(Integer id);

    @Delete("DELETE FROM department WHERE id = #{id}")
    int deleteDepartmentById(Integer id);

    @Options(useGeneratedKeys = true, keyProperty = "id") //返回自增主键
    @Insert("INSERT INTO department(departmentName) VALUES (#{departmentName})")
    int insertDepartment(Department department);

    @Update("UPDATE department SET departmentName = #{departmentName} WHERE id = #{id}")
    Department updateDepartment(Department department);
}
