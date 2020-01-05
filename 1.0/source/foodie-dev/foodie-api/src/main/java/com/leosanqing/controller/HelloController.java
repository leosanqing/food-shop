package com.leosanqing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author: leosanqing
 * @Date: 2019-12-04 22:03
 */
@RestController()
@ApiIgnore
public class HelloController {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("leosanqing")
    public String hello() {

        logger.info("hello");
        return "hello";
    }
}
