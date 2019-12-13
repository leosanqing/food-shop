package com.leosanqing.service;

import com.leosanqing.pojo.UserAddress;

import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-13 22:22
 */
public interface AddressService {

    /**
     * 查询所有地址
     * @param userId
     * @return
     */
    List<UserAddress> queryAll(String userId);
}
