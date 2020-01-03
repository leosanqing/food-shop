package com.leosanqing.service.center.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leosanqing.enums.YesOrNo;
import com.leosanqing.mapper.ItemsCommentsMapperCustom;
import com.leosanqing.mapper.OrderItemsMapper;
import com.leosanqing.mapper.OrderStatusMapper;
import com.leosanqing.mapper.OrdersMapper;
import com.leosanqing.pojo.OrderItems;
import com.leosanqing.pojo.OrderStatus;
import com.leosanqing.pojo.Orders;
import com.leosanqing.pojo.bo.center.OrderItemsCommentBO;
import com.leosanqing.pojo.vo.MyCommentVO;
import com.leosanqing.service.center.MyCommentsService;
import com.leosanqing.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019/12/22 下午4:09
 * @Package: com.leosanqing.service.center.impl
 * @Description: 我的评价服务实现
 */

@Service
public class MyCommentsServiceImpl implements MyCommentsService {

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private Sid sid;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<OrderItems> queryPendingComment(String orderId) {

        final OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);

        return orderItemsMapper.select(orderItems);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveComments(String userId, String orderId, List<OrderItemsCommentBO> orderItemList) {


        // 1.保存订单评价 item_comments
        for (OrderItemsCommentBO item : orderItemList) {
            item.setCommentId(sid.nextShort());
        }

        final HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("commentList",orderItemList);

        itemsCommentsMapperCustom.saveComments(map);


        // 2.修改订单 Orders

        final Orders orders = new Orders();
        orders.setIsComment(YesOrNo.YES.type);
        orders.setId(orderId);
        ordersMapper.updateByPrimaryKeySelective(orders);

        // 3. 修改订单状态的  commentTime

        final OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCommentTime(new Date());
        orderStatus.setOrderId(orderId);
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);


    }
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {

        final HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);

        PageHelper.startPage(page,pageSize);
        final List<MyCommentVO> myCommentVOS = itemsCommentsMapperCustom.queryMyComments(map);
        return  setterPage(myCommentVOS,page);
    }

    private PagedGridResult setterPage(List<?> list, int page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }

}
