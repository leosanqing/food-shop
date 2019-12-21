package com.leosanqing.mapper;

import com.leosanqing.my.mapper.MyMapper;
import com.leosanqing.pojo.Orders;
import com.leosanqing.pojo.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom {
    List<MyOrdersVO> queryMyOrders(
            @Param("paramsMap") Map<String, Object> map);
}