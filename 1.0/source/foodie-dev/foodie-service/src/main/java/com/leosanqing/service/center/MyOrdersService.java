package com.leosanqing.service.center;

import com.leosanqing.pojo.Orders;
import com.leosanqing.pojo.vo.OrderStatusCountsVO;
import com.leosanqing.utils.PagedGridResult;

/**
 * @Author: leosanqing
 * @Date: 2019/12/21 下午10:42
 * @Package: com.leosanqing.service.center
 * @Description: 我的订单相关服务
 */
public interface MyOrdersService {

    /**
     * 查询我的订单
     *
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyOrders(String userId,
                                  Integer orderStatus,
                                  Integer page,
                                  Integer pageSize);

    /**
     * @Description: 订单状态 --> 商家发货
     */
    void updateDeliverOrderStatus(String orderId);

    /**
     * 确认收货
     *
     * @param orderId
     */
    boolean confirmReceive(String orderId);

    /**
     * 删除订单
     * @param userId
     * @param orderId
     * @return
     */
    boolean deleteOrder(String userId, String orderId);


    /**
     * 查询订单
     *
     * @param userId
     * @param orderId
     * @return
     */
    Orders queryMyOrder(String userId, String orderId);

    /**
     * 查询用户订单数
     * @param userId
     * @return
     */
    OrderStatusCountsVO getOrderStatusCount(String userId);

    PagedGridResult getMyOrderTrend(String userId,
                    Integer page,
                    Integer pageSize);
}
