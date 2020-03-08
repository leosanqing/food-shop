package com.leosanqing.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: leosanqing
 * @Date: 2020/3/7 下午8:52
 * @Package: com.leosanqing.service
 * @Description: FastDFS服务接口
 */
public interface FdfsService {
    String upload(MultipartFile file, String fileExtendsName) throws IOException;
}
