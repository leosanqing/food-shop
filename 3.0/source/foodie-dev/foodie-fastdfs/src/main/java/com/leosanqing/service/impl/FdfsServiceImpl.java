package com.leosanqing.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leosanqing.service.FdfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: leosanqing
 * @Date: 2020/3/7 下午8:53
 * @Package: com.leosanqing.service.impl
 * @Description: FastDFS服务实现类
 */
@Service
public class FdfsServiceImpl implements FdfsService {

    @Autowired
    private FastFileStorageClient client;

    @Override
    public String upload(MultipartFile file, String fileExtendsName) throws IOException {
        StorePath storePath = client.uploadFile(file.getInputStream(),
                file.getSize(), fileExtendsName, null);

        String fullPath = storePath.getFullPath();
        return fullPath;
    }
}
