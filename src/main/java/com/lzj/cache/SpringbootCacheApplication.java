package com.lzj.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 缓存使用：
 *  1、开启缓存注解 @EnableCaching
 *  2、标注缓存注解即可
 *     @Cacheable
 *     @CacheEvict
 *     @CachePut
 *默认使用的是ConcurrentMapCacheManager==ConcurrentMapCache;将数据保存在ConcurrentMap<Object, Object> store中。
 *开发中使用缓存中间件：redis、memcached、ehcache;
 *
 *  整合Redis作为缓存
 *  1、安装redis, docker安装。
 *  2、springboot整合redis,引入starter
 *  3、配置redis
 *  4、测试缓存
 *      原理：CacheManager====Cache缓存组件实际给缓存中存取数据
 *      1）、引入了redis的starter,容器中保存的是RedisCacheManager;原本的ConcurrentMapCacheManager不会被创建了。
 *      2)、RedisCacheManager帮我们创建RedisCache作为缓存组件；RedisCache通过redis操作缓存数据。
 *      3）、RedisTemplate<Object, Object>默认保存的K-V都是object，默认都是使用的JDK序列化机制。
 *      4)、自定义cacheManager，更改默认的序列化方法。
 */


@SpringBootApplication
@MapperScan(basePackages = {"com.lzj.cache.mapper"}) //扫描mapper接口
@EnableCaching //开启缓存注解
public class SpringbootCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootCacheApplication.class, args);
    }

}

