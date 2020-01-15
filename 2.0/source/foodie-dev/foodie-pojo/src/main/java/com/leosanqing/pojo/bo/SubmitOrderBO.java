package com.leosanqing.pojo.bo;

import lombok.Data;

/**
 * @Author: leosanqing
 * @Date: 2019-12-14 15:05
 * @Package: com.leosanqing.pojo.bo
 * @Description: 提交订单对象
 */
@Data
public class SubmitOrderBO {
    private String userId;
    private String itemSpecIds;
    private String addressId;
    private Integer payMethod;
    private String leftMsg;
}
