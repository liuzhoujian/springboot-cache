package com.lzj.cache.controller;

import com.lzj.cache.bean.Employee;
import com.lzj.cache.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmpController {

    @Autowired
    private EmpService empService;

    @GetMapping("/getEmp/{id}")
    public Employee getEmp(@PathVariable("id") Integer id) {
        return empService.getEmp(id);
    }

    @GetMapping("/updateEmp")
    public Employee updateEmp(Employee employee) {
        return empService.updateEmp(employee);
    }

    @GetMapping("/deleteEmp/{id}")
    public String deleteEmp(@PathVariable("id") Integer id) {
        empService.deleteEmp(id);
        return "success";
    }

    @GetMapping("/getEmp/lastName/{lastName}")
    public Employee getEmpByLastName(@PathVariable("lastName") String lastName) {
        return empService.getEmpByLastName(lastName);
    }

}
