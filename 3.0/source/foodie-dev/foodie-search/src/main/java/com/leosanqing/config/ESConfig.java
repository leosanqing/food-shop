package com.leosanqing.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Author: leosanqing
 * @Date: 2020/3/3 下午11:26
 * @Package: com.leosanqing.config
 * @Description: ES 的配置类
 */

@Configuration
public class ESConfig {

    @PostConstruct
    public void init(){
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
