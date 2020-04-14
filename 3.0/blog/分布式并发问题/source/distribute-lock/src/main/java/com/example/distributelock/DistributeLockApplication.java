package com.example.distributelock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.example.distributelock.dao")
@EnableScheduling
//@ImportResource("classpath*:redisson.xml")
public class DistributeLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributeLockApplication.class, args);
    }



    @Bean(initMethod="start",destroyMethod = "close")
    public CuratorFramework getCuratorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        return CuratorFrameworkFactory.newClient("10.211.55.12:2181", retryPolicy);
    }

}
