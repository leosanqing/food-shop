package com.leosanqing.service;

import com.leosanqing.pojo.OrderStatus;
import com.leosanqing.pojo.bo.ShopCartBO;
import com.leosanqing.pojo.bo.SubmitOrderBO;
import com.leosanqing.pojo.vo.OrderVO;

import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-15 12:30
 * @Package: com.leosanqing.service
 * @Description: 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单
     * @param submitOrderBO
     * @param shopCartBOList
     * @return
     */
    OrderVO createOrder(List<ShopCartBO> shopCartBOList, SubmitOrderBO submitOrderBO);


    /**
     * 根据订单id 查询订单状态
     * @param orderId
     * @return
     */
    OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭超时未支付订单
     */
    void closeOrder();
}
