package com.leosanqing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: leosanqing
 * @Date: 2020/1/15 上午8:05
 * @Package: com.leosanqing.controller
 * @Description: Redis测试类
 */
@RequestMapping("redis")
@RestController
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("set")
    public String set( String key,  String value){
        redisTemplate.opsForValue().set(key,value);
        return "ok";
    }

    @GetMapping("get")
    public String get( String key){
        return (String)redisTemplate.opsForValue().get(key);
    }
}
