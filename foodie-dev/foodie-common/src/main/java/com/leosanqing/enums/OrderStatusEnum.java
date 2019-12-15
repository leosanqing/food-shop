package com.leosanqing.enums;

/**
 * @Author: leosanqing
 * @Date: 2019-12-15 14:08
 * @Package: com.leosanqing.enums
 * @Description: 订单状态枚举类
 */
public enum OrderStatusEnum {
    WAIT_PAY(10,"待付款"),
    WAIT_DELIVER(20,"待发货"),
    WAIT_RECEIVE(30,"待收货"),
    SUCCESS(40,"交易成功"),
    CLOSE(50,"交易失败")
    ;


    public final Integer type;
    public final String value;

    OrderStatusEnum(Integer type,String value){
        this.type = type;
        this.value = value;
    }
}
