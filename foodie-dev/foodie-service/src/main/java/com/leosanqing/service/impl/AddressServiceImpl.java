package com.leosanqing.service.impl;

import com.leosanqing.mapper.UserAddressMapper;
import com.leosanqing.pojo.UserAddress;
import com.leosanqing.pojo.Users;
import com.leosanqing.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-13 22:30
 * @Package: com.leosanqing.service.impl
 * @Description: TODO
 */
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {

        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }
}
