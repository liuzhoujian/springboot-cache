package com.lzj.cache.service;

import com.lzj.cache.bean.Employee;
import com.lzj.cache.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

@Service
@CacheConfig
public class EmpService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * @Cacheable 将方法的运行结果进行缓存；第二次再要相同的数据，直接从缓存中获取，不再调用方法；
     *
     * CacheManager管理多个Cache组件，对缓存的真正CRUD操作在Cache组件中，每一个缓存组件有自己唯一的名字。
     * 几个属性：
     *      cacheNames/value:指定缓存组件的名字， 数组的方式，可以指定多个缓存
     *      key: 缓存数据使用的key;key用它来指定。默认使用方法参数的值作为key
     *           编写SpEL； #root.methodName ...
     *      keyGenerator: key的生成器；可以自己指定key的生成器的组件id.
     *      key和keyGenerator二选一。
     *      cacheManager: 指定缓存管理器。或者指定cacheResolver缓存解析器。二选一。
     *      condition： 指定符合条件的情况下才缓存；如：, condition = "#id>0" "#a0>1"才进行缓存
     *      unless: 否定缓存； 当unless指定的条件为true,方法的返回值就不会缓存；可以获取到结果进行判断
     *              unless = "#result == null "  当方法结果为null时，不缓存。
     *      sync: 是否使用异步模式
     *
     *原理：
     * 1、自动配置类 CacheAutoConfiguration
     * 2、缓存的配置类
     *      0 = "org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration"
     *      1 = "org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration"
     *      2 = "org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration"
     *      3 = "org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration"
     *      4 = "org.springframework.boot.autoconfigure.cache.InfinispanCacheConfiguration"
     *      5 = "org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration"
     *      6 = "org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration"
     *      7 = "org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration"
     *      8 = "org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration"【默认】
     *      9 = "org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration"
     * 3、哪个配置类生效？
     *    默认SimpleCacheConfiguration生效，给容器中注册了一个CacheManager，叫ConcurrentMapCacheManager
     *            ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap(16);以键值对存储缓存
     * 4、ConcurrentMapCacheManager 可以获取和创建ConcurrentMapCache类型的缓存组件；他的作用将数据保存在ConcurrentMap<Object, Object> store中。
     *
     * 运行流程：
     * @Cacheable
     * 1、方法运行之前，先去查询Cache(缓存组件），按照cacheNames指定的名字获取；（CacheManager先获取相应的缓存），
     *  第一次获取缓存如果没有Cache,会自动创建缓存组件。
     * 2、去Cache中查找缓存的内容，使用一个Key,默认：方法的参数
     *    key是按照某种策略生成的。默认是使用keyGenerator生成的。默认使用SimpleKeyGenerator生成key.
     *      simpleKeyGenerator生成key的默认策略：
     *        如果没有参数： key = new SimpleKey();
     *        如果有一个参数： key = 参数的值
     *        如果有多个参数： key=new SimpleKey(params)
     *
     * 3、没有查到缓存就调用目标方法
     * 4、将目标方法返回的结果放到缓存中。
     *
     * 总结：@Cacheable标注的方法执行前先检查缓存中有没有这个数据，默认按照参数的值作为key去查询缓存，如果没有就运行方法并将结果放入缓存。
     * 以后再来调用就直接使用缓存中的数据。
     *
     * 核心：
     *   1、使用CacheManager[ConcurrentMapCacheManager]按照名字得到Cache[ConcurrentMapCache]组件
     *   2、key使用keyGenerator生成，默认是simpleKeyGenerator
     *
     *
     * 测试：
     *   key的生成方式：
     *     第一种： 使用SPEL指定key  key="#root.methodName+'['+ #id +']'" ,使用getEmp[id]作为key
     *     第二种： keyGenerator
     */
    @Cacheable(cacheNames= {"emp"},unless="#result == null") //当结果为空时不缓存
    public Employee getEmp(Integer id) {
        System.out.println("查询【" + id + "】号员工");
        return employeeMapper.getEmpById(id);
    }

    /**
     * @CachePut:既调用方法，又更新缓存。
     * 修改了数据库的某个数据，又更新缓存
     * 运行时机：先调用目标方法，将目标方法的结果缓存起来
     *
     * 测试步骤：
     *  1、首先查询1号员工，会查询数据库  {"id":1,"lastName":"张三","gender":1,"email":"zhangsan@qq.com","did":1}
     *  2、再次查询1号员工，会走缓存     {"id":1,"lastName":"张三","gender":1,"email":"zhangsan@qq.com","did":1}
     *  3、修改1号员工的信息            {"id":null,"lastName":"lsi","gender":0,"email":"lis?@qq.com","did":null}
     *  4、再次查询1号员工？            {"id":1,"lastName":"张三","gender":1,"email":"zhangsan@qq.com","did":1}
     *    发现走的缓存，但是缓存的还是旧的数据，并没有更新。
     *
     *  为啥会这样？
     *  原因：
     *   查询1号员工时，key为id, 值为： {"id":1,"lastName":"张三","gender":1,"email":"zhangsan@qq.com","did":1}
     *   当修改员工后，将修改的数据存入缓存， key为employee对象，值为 {"id":null,"lastName":"lsi","gender":0,"email":"lis?@qq.com","did":null}
     *   并没有更新原来key为id的缓存
     *   解决方法：
     *    key="#result.id" 或者 key = "#employee.id"
     *
     */
    @CachePut(cacheNames = {"emp"}, key = "#employee.id") //注意：修改缓存时，key要和放入的相同。
    public Employee updateEmp(Employee employee) {
        System.out.println("updateEmp: " + employee);
        employeeMapper.updateEmp(employee);
        return employee;
    }


    /**
     * @CacheEvit：缓存清除
     *
     * allEntries = true 每次删除，将指定emp的缓存中的所有数据全都删除
     * beforeInvocation=false ,缓存的清除是否是在方法之前执行，默认false, 即在方法之后清除，当方法执行出现异常时，缓存不会清除。
     * beforeInvocation=true ,方法之前清除，无论方法执行是否出现异常，缓存都会清除。
     */
    @CacheEvict(cacheNames = {"emp"},/* key = "#id",*/ allEntries = true)
    public void deleteEmp(Integer id) {
        System.out.println("删除【" + id + "】");
        //employeeMapper.deleteEmp(id);
    }


    /**
     * caching组合注解
     * 当使用lastName查询用户时，会将用户以lastName为键存入缓存。
     * 并且运行之后，以id和email为键存储用户
     *
     * 即：   张三        :    {"id":1,"lastName":"张三","gender":1,"email":"zhangsan@qq.com","did":1}
     *   zhangsan@qq.com :    {"id":1,"lastName":"张三","gender":1,"email":"zhangsan@qq.com","did":1}
     *      1            :    {"id":1,"lastName":"张三","gender":1,"email":"zhangsan@qq.com","did":1}
     *
     * 当@cacheable和@CachePut同时使用时，每次都会执行目标方法
     *
     */

    @Caching(
            cacheable = {
                    @Cacheable(cacheNames = "emp", key = "#lastName")
            },

            put = {
                    @CachePut(cacheNames = "emp", key = "#result.id"),
                    @CachePut(cacheNames = "emp", key = "#result.email")
            }
    )
    public Employee getEmpByLastName(String lastName) {
        return employeeMapper.getEmpByLastName(lastName);
    }

}
