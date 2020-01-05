package com.leosanqing.service.impl;

import com.leosanqing.enums.YesOrNo;
import com.leosanqing.mapper.UserAddressMapper;
import com.leosanqing.pojo.UserAddress;
import com.leosanqing.pojo.bo.AddressBO;
import com.leosanqing.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-13 22:30
 * @Package: com.leosanqing.service.impl
 * @Description: 收货地址相关服务
 */
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {

        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addNewUserAddress(AddressBO addressBO) {

        Integer isDefault = 0;
        // 查询之前是否存在地址
        List<UserAddress> addressList = queryAll(addressBO.getUserId());
        if (null == addressList || addressList.isEmpty() || addressList.size() == 0) {
            isDefault = 1;
        }

        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, userAddress);
        userAddress.setId(sid.nextShort());
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.insert(userAddress);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserAddress(AddressBO addressBO) {

        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO,userAddress);

        String addressId = addressBO.getAddressId();
        userAddress.setId(addressId);
        userAddress.setUpdatedTime(new Date());

        // 这样空值不会覆盖数据库中已有的数据
        userAddressMapper.updateByPrimaryKeySelective(userAddress);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserAddress(String userId, String addressId) {

        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setId(addressId);

        userAddressMapper.delete(userAddress);
    }

    @Override
    public void updateToBeDefault(String userId, String addressId) {

        // 将原来的地址修改为非默认地址
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setIsDefault(YesOrNo.YES.type);
        List<UserAddress> select = userAddressMapper.select(userAddress);

        for (UserAddress address : select) {
            address.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(address);
        }


        // 将现在的地址改为默认地址

        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setUserId(userId);
        defaultAddress.setIsDefault(YesOrNo.YES.type);
        defaultAddress.setId(addressId);

        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
    }

    @Override
    public UserAddress queryAddress(String userId, String addressId) {
        final UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setId(addressId);
        return userAddressMapper.selectOne(userAddress);
    }
}
