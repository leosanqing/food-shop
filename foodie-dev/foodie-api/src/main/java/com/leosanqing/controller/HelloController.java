package com.leosanqing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author: leosanqing
 * @Date: 2019-12-04 22:03
 */
@RestController()
@ApiIgnore
public class HelloController {

    @GetMapping("leosanqing")
    public String hello(){
        return "hello";
    }
}
