package com.leosanqing.controller;

import com.leosanqing.utils.RedisOperator;
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
    private RedisOperator redisOperator;

    @GetMapping("set")
    public String set( String key,  String value){
        redisOperator.set(key, value);
        return "ok";
    }

    @GetMapping("get")
    public String get( String key){
        return redisOperator.get(key);
    }
}
