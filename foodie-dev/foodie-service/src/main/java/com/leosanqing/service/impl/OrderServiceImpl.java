package com.leosanqing.service.impl;

import com.leosanqing.enums.OrderStatusEnum;
import com.leosanqing.enums.YesOrNo;
import com.leosanqing.mapper.OrderItemsMapper;
import com.leosanqing.mapper.OrderStatusMapper;
import com.leosanqing.mapper.OrdersMapper;
import com.leosanqing.pojo.*;
import com.leosanqing.pojo.bo.SubmitOrderBO;
import com.leosanqing.service.AddressService;
import com.leosanqing.service.ItemService;
import com.leosanqing.service.OrderService;
import com.leosanqing.utils.DateUtil;
import com.leosanqing.utils.JSONResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-15 12:31
 * @Package: com.leosanqing.service.impl
 * @Description: TODO
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void closeOrder() {

        // 查询所有未付款订单，判断时间是否超时（1天），超时则关闭交易
        OrderStatus queryOrder = new OrderStatus();
        queryOrder.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> list = orderStatusMapper.select(queryOrder);
        for (OrderStatus os : list) {
            // 获得订单创建时间
            Date createdTime = os.getCreatedTime();
            // 和当前时间进行对比
            int days = DateUtil.daysBetween(createdTime, new Date());
            if (days >= 1) {
                // 超过1天，关闭订单
                doCloseOrder(os.getOrderId());
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    void doCloseOrder(String orderId) {
        OrderStatus close = new OrderStatus();
        close.setOrderId(orderId);
        close.setOrderStatus(OrderStatusEnum.CLOSE.type);
        close.setCloseTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(close);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String createOrder(SubmitOrderBO submitOrderBO) {
        final String userId = submitOrderBO.getUserId();
        final String itemSpecIds = submitOrderBO.getItemSpecIds();
        final String addressId = submitOrderBO.getAddressId();
        final String leftMsg = submitOrderBO.getLeftMsg();
        final Integer payMethod = submitOrderBO.getPayMethod();

        Integer postAmount = 0;

        // 1.生成 新订单 ，填写 Orders表
        final String orderId = sid.nextShort();
        final Orders orders = new Orders();
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setLeftMsg(leftMsg);
        orders.setPayMethod(payMethod);

        final UserAddress userAddress = addressService.queryAddress(userId, addressId);
        orders.setReceiverAddress(userAddress.getProvince() + " " + userAddress.getCity() + " "
                + userAddress.getDistrict() + " " + userAddress.getDetail());
        orders.setReceiverMobile(userAddress.getMobile());
        orders.setReceiverName(userAddress.getReceiver());

        orders.setPostAmount(postAmount);

        orders.setIsComment(YesOrNo.NO.type);
        orders.setIsDelete(YesOrNo.NO.type);
        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());


        // 2.1 循环根据商品规格表，保存到商品规格表

        int totalAmount = 0;
        int realPayTotalAmount = 0;
        final String[] itemSpecIdArray = StringUtils.split(itemSpecIds, ',');
        for (String itemSpecId : itemSpecIdArray) {

            // 查询每个商品的规格

            final ItemsSpec itemsSpec = itemService.queryItemBySpecId(itemSpecId);

            // TODO 根据redis获取总共有多少件商品，现在只设置成1
            int counts = 1;
            // 获取价格
            totalAmount += itemsSpec.getPriceNormal() * counts;
            realPayTotalAmount += itemsSpec.getPriceDiscount() * counts;

            // 2.2 根据商品id，获得商品图片和信息
            final String itemId = itemsSpec.getItemId();
            final String imgUrl = itemService.queryItemImgByItemId(itemId);

            // 2.3 将商品规格信息写入 订单商品表
            final OrderItems subOrderItem = new OrderItems();
            subOrderItem.setBuyCounts(counts);
            subOrderItem.setItemImg(imgUrl);
            subOrderItem.setItemId(itemId);
            subOrderItem.setId(sid.nextShort());
            subOrderItem.setItemName(itemsSpec.getName());
            subOrderItem.setItemSpecId(itemSpecId);
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemSpecName(itemsSpec.getName());
            subOrderItem.setPrice(itemsSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItem);


            // 2.4 减库存
            itemService.decreaseItemSpecStock(itemSpecId, counts);

        }

        orders.setTotalAmount(totalAmount);
        orders.setRealPayAmount(realPayTotalAmount);

        ordersMapper.insert(orders);

        // 3. 订单状态表
        final OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        try {
            Thread.sleep(3 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_DELIVER.type);
        orderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(orderStatus);

        return orderId;
    }
}
