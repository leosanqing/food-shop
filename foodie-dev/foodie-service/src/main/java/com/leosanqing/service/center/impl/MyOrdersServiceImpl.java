package com.leosanqing.service.center.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leosanqing.mapper.OrdersMapperCustom;
import com.leosanqing.pojo.vo.MyOrdersVO;
import com.leosanqing.service.center.MyOrdersService;
import com.leosanqing.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
}
