package com.leosanqing.service.center;

import com.leosanqing.pojo.OrderItems;
import com.leosanqing.pojo.bo.center.OrderItemsCommentBO;
import com.leosanqing.pojo.vo.MyCommentVO;
import com.leosanqing.utils.PagedGridResult;

import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019/12/22 下午4:08
 * @Package: com.leosanqing.service.center
 * @Description: 我的评价服务接口
 */
public interface MyCommentsService {
    /**
     * 查询商品
     * @param orderId
     * @return
     */
    List<OrderItems> queryPendingComment(String orderId);


    /**
     * 保存订单评价列表
     * @param userId
     * @param orderId
     * @param orderItemList
     */
    void saveComments(String userId, String orderId, List<OrderItemsCommentBO> orderItemList);


    /**
     * 查询我的评价列表
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);
}
