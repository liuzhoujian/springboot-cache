package com.lzj.cache;

import com.lzj.cache.bean.Department;
import com.lzj.cache.bean.Employee;
import com.lzj.cache.mapper.DepartmentMapper;
import com.lzj.cache.mapper.EmployeeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootCacheApplicationTests {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    RedisTemplate redisTemplate; //k-v 都是对象

    @Autowired
    StringRedisTemplate stringRedisTemplate; //操作k-v都是字符串


    @Qualifier("myRedisTemplate")
    @Autowired
    RedisTemplate<Object, Object> myRedisTemplate;


     /*
        StringRedisTemplate相关操作：
        stringRedisTemplate.opsForValue();
        stringRedisTemplate.opsForList();
        stringRedisTemplate.opsForHash();
        stringRedisTemplate.opsForSet();
        stringRedisTemplate.opsForZSet();
     */


    @Test
    public void test01() {
        //stringRedisTemplate.opsForValue().append("msg", "hello");
        String res = stringRedisTemplate.opsForValue().get("msg");
        System.out.println(res);
    }

    @Test
    public void test02() {
        //对象操作
        Employee employee = employeeMapper.getEmpById(1);
        //默认使用JDK序列化机制，将对象序列化后保存在redis中
       // redisTemplate.opsForValue().set("emp-1", employee);


        //如何将对象以JSON保存在redis
        //(1)自己手动将对象转为JSON，再存入Redis
        //(2)redisTemplate默认序列化规则
        myRedisTemplate.opsForValue().set("emp-1", employee);

        Department department = departmentMapper.getDepartmentById(1);
        myRedisTemplate.opsForValue().set("dep-1", department);

//        genericRedisTemplate.opsForValue().set("emp-2", employee);
//        genericRedisTemplate.opsForValue().set("dep-2", department);

    }

    @Test
    public void contextLoads() {
    }

}

