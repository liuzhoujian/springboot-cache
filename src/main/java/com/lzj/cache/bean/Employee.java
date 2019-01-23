package com.lzj.cache.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Employee implements Serializable {

    private Integer id;
    private String lastName;
    private Integer gender;
    private String email;
    private Integer dId;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", dId=" + dId +
                '}';
    }
}
