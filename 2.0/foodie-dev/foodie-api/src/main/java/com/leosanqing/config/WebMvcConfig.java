package com.leosanqing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: leosanqing
 * @Date: 2019/12/16 上午8:22
 * @Package: com.leosanqing.config
 * @Description: TODO
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                // swagger2的映射路径
                .addResourceLocations("classpath:/META-INF/resources/")
                // 图片的映射路径
                .addResourceLocations("file:/Users/zhuerchong/Desktop/code/idea/foodie-dev/img");
    }
}
