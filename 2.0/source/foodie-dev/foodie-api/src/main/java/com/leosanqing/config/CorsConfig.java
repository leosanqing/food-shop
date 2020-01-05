package com.leosanqing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author: leosanqing
 * @Date: 2019-12-07 16:41
 */

@Configuration
public class CorsConfig {

    public CorsConfig(){}

    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://localhost");
        config.addAllowedOrigin("http://10.211.55.3:8080");
        config.addAllowedOrigin("http://10.211.55.3");
        config.addAllowedOrigin("http://shop.z.foodie.com:8080");
        config.addAllowedOrigin("http://center.z.foodie.com:8080");
        config.addAllowedOrigin("http://shop.z.foodie.com");
        config.addAllowedOrigin("http://center.z.foodie.com");
        config.addAllowedOrigin("*");


        config.setAllowCredentials(true);
        config.addAllowedMethod("*");

        //设置允许的header
        config.addAllowedHeader("*");

        // 为url添加映射路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);

        return new CorsFilter(source);

    }

}
