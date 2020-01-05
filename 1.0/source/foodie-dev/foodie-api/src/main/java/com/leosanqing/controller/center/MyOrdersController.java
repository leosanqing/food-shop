package com.leosanqing.controller.center;

import com.leosanqing.pojo.Orders;
import com.leosanqing.pojo.Users;
import com.leosanqing.pojo.vo.OrderStatusCountsVO;
import com.leosanqing.service.center.CenterUserService;
import com.leosanqing.service.center.MyOrdersService;
import com.leosanqing.utils.JSONResult;
import com.leosanqing.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.HttpResource;

/**
 * @Author: leosanqing
 * @Date: 2019-12-15 20:00
 * @Package: com.leosanqing.controller.center
 * @Description: 用户中心订单Controller
 */
@Api(value = "我的订单-用户中心", tags = {"我的订单-用户中心展示的相关接口"})
@RestController
@RequestMapping("myorders")
public class MyOrdersController {

    @Autowired
    private MyOrdersService myOrdersService;

    @PostMapping("query")
    @ApiOperation(value = "查询我的订单", notes = "查询我的订单", httpMethod = "POST")
    public JSONResult queryUserInfo(
            @ApiParam(name = "userId", value = "用户id")
            @RequestParam String userId,
            @ApiParam(name = "orderStatus", value = "订单状态")
            @RequestParam Integer orderStatus,
            @ApiParam(name = "page", value = "当前页数")
            @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(name = "pageSize", value = "页面展示条数")
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户名id为空");
        }

        final PagedGridResult pagedGridResult = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);
        return JSONResult.ok(pagedGridResult);
    }


    @PostMapping("trend")
    @ApiOperation(value = "查询我的订单", notes = "查询我的订单", httpMethod = "POST")
    public JSONResult getTrend(
            @ApiParam(name = "userId", value = "用户id")
            @RequestParam String userId,
            @ApiParam(name = "page", value = "当前页数")
            @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(name = "pageSize", value = "页面展示条数")
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户名id为空");
        }

        final PagedGridResult pagedGridResult = myOrdersService.getMyOrderTrend(userId, page, pageSize);
        return JSONResult.ok(pagedGridResult);
    }




    // 商家发货没有后端，所以这个接口仅仅只是用于模拟
    @ApiOperation(value="商家发货", notes="商家发货", httpMethod = "GET")
    @GetMapping("/deliver")
    public JSONResult deliver(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) {

        if (StringUtils.isBlank(orderId)) {
            return JSONResult.errorMsg("订单ID不能为空");
        }
        myOrdersService.updateDeliverOrderStatus(orderId);
        return JSONResult.ok();
    }



    @ApiOperation(value="确认收货", notes="确认收货", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public JSONResult confirmReceive(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) {

        final JSONResult result = checkUserOrder(userId, orderId);
        if (result.getStatus() != HttpStatus.OK.value()){

            return result;
        }
        final boolean isSuccess = myOrdersService.confirmReceive(orderId);
        if(!isSuccess){
            return JSONResult.errorMsg("确认收货失败");
        }
        return JSONResult.ok();
    }


    @ApiOperation(value="删除订单", notes="删除订单", httpMethod = "POST")
    @PostMapping("/delete")
    public JSONResult deleteOrder(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) {

        final JSONResult result = checkUserOrder(userId, orderId);
        if (result.getStatus() != HttpStatus.OK.value()){
            return result;
        }
        final boolean isSuccess = myOrdersService.deleteOrder(userId, orderId);
        if(!isSuccess){
            return JSONResult.errorMsg("删除订单失败");
        }
        return JSONResult.ok();
    }


    @ApiOperation(value="删除订单", notes="删除订单", httpMethod = "POST")
    @PostMapping("/statusCounts")
    public JSONResult statusCounts(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) {
        if(StringUtils.isBlank(userId)){
            return JSONResult.errorMsg("用户Id为空");
        }

        final OrderStatusCountsVO count = myOrdersService.getOrderStatusCount(userId);
        return JSONResult.ok(count);
    }

    /**
     * 用于验证是否为用户订单，防止恶意查询
     * @param userId
     * @param orderId
     * @return
     */
    private JSONResult checkUserOrder(String userId,String orderId){
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户ID不能为空");
        }
        if (StringUtils.isBlank(orderId)) {
            return JSONResult.errorMsg("订单ID不能为空");
        }
        final Orders orders = myOrdersService.queryMyOrder(userId, orderId);
        if(orders == null){
            return JSONResult.errorMsg("查询到订单为空");
        }
        return JSONResult.ok();
    }







}
