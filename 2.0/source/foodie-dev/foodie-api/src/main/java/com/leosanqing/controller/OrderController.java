package com.leosanqing.controller;

import com.leosanqing.pojo.OrderStatus;
import com.leosanqing.pojo.bo.ShopCartBO;
import com.leosanqing.pojo.bo.SubmitOrderBO;
import com.leosanqing.pojo.vo.OrderVO;
import com.leosanqing.service.OrderService;
import com.leosanqing.utils.CookieUtils;
import com.leosanqing.utils.JSONResult;
import com.leosanqing.utils.JsonUtils;
import com.leosanqing.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-14 14:56
 * @Package: com.leosanqing.controller
 * @Description: 订单相关Controller
 */
@RestController
@RequestMapping("orders")
@Api(value = "订单相关", tags = {"订单的相关接口"})
public class OrderController extends BaseController{

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisOperator redisOperator;

    @PostMapping("create")
    @ApiOperation(value = "创建订单", notes = "创建订单", httpMethod = "POST")
    public JSONResult subCats(
            @ApiParam(name = "submitOrderBO", value = "订单对象", required = true)
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response) {


        final String shopCartStr = redisOperator.get(SHOP_CART + ":" + submitOrderBO.getUserId());
        if(StringUtils.isNotBlank(shopCartStr)){
            return JSONResult.errorMsg("购物车数据不正确");
        }
        List<ShopCartBO> shopCartBOList = JsonUtils.jsonToList(shopCartStr, ShopCartBO.class);


        // 1.创建订单
        OrderVO orderVO = orderService.createOrder(shopCartBOList,submitOrderBO);
        String orderId = orderVO.getOrderId();

        // 2.创建订单以后，移除购物车中已结算的商品
        if (shopCartBOList != null) {
            shopCartBOList.removeAll(orderVO.getToBeRemovedList());
        }
        redisOperator.set(SHOP_CART+":"+submitOrderBO.getUserId(),JsonUtils.objectToJson(shopCartBOList));

        CookieUtils.setCookie(request,response,SHOP_CART,JsonUtils.objectToJson(shopCartBOList), true);

        // TODO 整合Redis之后
        // 3.像支付中心发送当前订单，用于保存支付中心的订单数据
        System.out.println(submitOrderBO.toString());
        return JSONResult.ok(orderId);

    }


    @PostMapping("getPaidOrderInfo")
    @ApiOperation(value = "查询支付状态", notes = "查询支付状态", httpMethod = "POST")
    public JSONResult getPaidOrderInfo(String orderId) {

        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return JSONResult.ok(orderStatus);
    }



}
