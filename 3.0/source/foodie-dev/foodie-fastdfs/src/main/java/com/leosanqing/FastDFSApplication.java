package com.leosanqing;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: leosanqing
 * @Date: 2019-12-03 23:24
 */
@SpringBootApplication
// 扫描通用mapper配置
@MapperScan(basePackages = "com.leosanqing.mapper")
// 扫描组件包
@ComponentScan(basePackages = {"com.leosanqing","org.n3r.idworker"})
public class FastDFSApplication {
    public static void main(String[] args) {
        SpringApplication.run(FastDFSApplication.class,args);
    }
}
