package com.leosanqing.resource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Author: leosanqing
 * @Date: 2019/12/15 下午11:28
 * @Package: com.leosanqing.resource
 * @Description: TODO
 */

@Component
@ConfigurationProperties(prefix = "file")
@PropertySource("classpath:file-upload-dev.properties")
@Data
public class FileUpload {
    private String userFaceImgLocation;
    private String imgServerUrl;

}
