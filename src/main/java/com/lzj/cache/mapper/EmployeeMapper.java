package com.lzj.cache.mapper;

import com.lzj.cache.bean.Employee;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EmployeeMapper {

    @Select("SELECT * FROM employee WHERE id = #{id}")
    Employee getEmpById(Integer id);

    @Insert("INSERT INTO employee(lastName, email, gender, d_id) VALUES (#{lastName}, #{gender}, #{email}, #{dId})")
    int insertEmp(Employee employee);

    @Update("UPDATE employee set lastName=#{lastName}, email=#{email}, gender=#{gender},d_id=#{dId}")
    int updateEmp(Employee employee);

    @Delete("DELETE FROM employee WHERE id = #{id}")
    int deleteEmp(Integer id);

    @Select("SELECT * FROM employee WHERE lastName = #{lastName}")
    Employee getEmpByLastName(String lastName);
}
