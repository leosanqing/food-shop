package com.leosanqing.service.center.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leosanqing.enums.OrderStatusEnum;
import com.leosanqing.enums.YesOrNo;
import com.leosanqing.mapper.OrderStatusMapper;
import com.leosanqing.mapper.OrdersMapper;
import com.leosanqing.mapper.OrdersMapperCustom;
import com.leosanqing.pojo.OrderStatus;
import com.leosanqing.pojo.Orders;
import com.leosanqing.pojo.vo.MyOrdersVO;
import com.leosanqing.pojo.vo.OrderStatusCountsVO;
import com.leosanqing.service.center.MyOrdersService;
import com.leosanqing.utils.JSONResult;
import com.leosanqing.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019/12/21 下午10:42
 * @Package: com.leosanqing.service.center.impl
 * @Description: 我的订单相关服务实现
 */
@Service
public class MyOrdersServiceImpl implements MyOrdersService {

    @Autowired
    private OrdersMapperCustom ordersMapperCustom;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private OrdersMapper ordersMapper;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", userId);
        if (orderStatus != null) {
            hashMap.put("orderStatus", orderStatus);
        }

        PageHelper.startPage(page, pageSize);
        final List<MyOrdersVO> myOrdersVOS = ordersMapperCustom.queryMyOrders(hashMap);


        return setterPage(myOrdersVOS, page);
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

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateDeliverOrderStatus(String orderId) {

        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        updateOrder.setDeliverTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);

        orderStatusMapper.updateByExampleSelective(updateOrder, example);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteOrder(String userId, String orderId) {

        final Orders orders = new Orders();
        orders.setIsDelete(YesOrNo.YES.type);
        orders.setUpdatedTime(new Date());

        final Example example = new Example(Orders.class);
        final Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("id", orderId);

        final int result = ordersMapper.updateByExampleSelective(orders, example);
        return result == 1;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean confirmReceive(String orderId) {
        final OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        orderStatus.setSuccessTime(new Date());

        final Example example = new Example(OrderStatus.class);
        final Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);

        final int result = orderStatusMapper.updateByExampleSelective(orderStatus, example);


        return result == 1;

    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Orders queryMyOrder(String userId, String orderId) {
        final Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setId(orderId);
        orders.setIsDelete(YesOrNo.NO.type);
        return ordersMapper.selectOne(orders);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public OrderStatusCountsVO getOrderStatusCount(String userId) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);

        map.put("orderStatus",OrderStatusEnum.WAIT_PAY.type);
        final int waitPayCount = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus",OrderStatusEnum.WAIT_DELIVER.type);
        final int waitDeliverCount = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus",OrderStatusEnum.WAIT_RECEIVE.type);
        final int waitReceiveCount = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus",OrderStatusEnum.SUCCESS.type);
        map.put("isComment",YesOrNo.NO.type);
        final int waitCommentCount = ordersMapperCustom.getMyOrderStatusCounts(map);

        final OrderStatusCountsVO orderStatusCountsVO = new OrderStatusCountsVO(waitPayCount, waitDeliverCount, waitReceiveCount, waitCommentCount);
        return orderStatusCountsVO;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult getMyOrderTrend(String userId, Integer page, Integer pageSize) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);

        PageHelper.startPage(page,pageSize);
        final List<OrderStatus> myOrderTrend = ordersMapperCustom.getMyOrderTrend(map);

        return setterPage(myOrderTrend,page);
    }
}
