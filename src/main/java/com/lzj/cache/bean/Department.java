package com.lzj.cache.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Department implements Serializable {

    private Integer id;
    private String departmentName;

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
