package com.leosanqing.service;

import com.leosanqing.pojo.UserAddress;
import com.leosanqing.pojo.bo.AddressBO;

import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-13 22:22
 */
public interface AddressService {

    /**
     * 查询所有地址
     *
     * @param userId
     * @return
     */
    List<UserAddress> queryAll(String userId);


    /**
     * 新增收货地址
     *
     * @param addressBO
     */
    void addNewUserAddress(AddressBO addressBO);

    /**
     * 修改收货地址
     *
     * @param addressBO
     */
    void updateUserAddress(AddressBO addressBO);

    /**
     * 删除收货地址
     * @param userId
     * @param addressId
     */
    void deleteUserAddress(String userId, String addressId);

    /**
     * 设置默认地址
     * @param userId
     * @param addressId
     */
    void updateToBeDefault(String userId,String addressId);


    /**
     * 根据用户id和地址id查询地址信息
     * @param userId
     * @param addressId
     * @return
     */
    UserAddress queryAddress(String userId,String addressId);
}
