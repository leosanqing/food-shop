package com.leosanqing.service.center;

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
}
