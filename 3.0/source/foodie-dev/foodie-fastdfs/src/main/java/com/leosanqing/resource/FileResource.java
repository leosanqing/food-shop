package com.leosanqing.resource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Author: leosanqing
 * @Date: 2020/3/8 上午11:08
 * @Package: com.leosanqing.resource
 * @Description: 文件资源配置读取
 */
@Component
@ConfigurationProperties(prefix = "file")
@PropertySource("classpath:file.properties")
@Data
public class FileResource {

    private String host;

}
