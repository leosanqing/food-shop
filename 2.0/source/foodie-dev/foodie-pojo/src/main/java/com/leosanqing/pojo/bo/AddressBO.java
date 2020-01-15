package com.leosanqing.pojo.bo;

import lombok.Data;

/**
 * @Author: leosanqing
 * @Date: 2019-12-14 09:03
 * @Package: com.leosanqing.pojo.bo
 * @Description: 地址对象
 */

@Data
public class AddressBO {
    private String addressId;
    private String userId;
    private String receiver;
    private String mobile;
    private String province;
    private String city;
    private String district;
    private String detail;
}
