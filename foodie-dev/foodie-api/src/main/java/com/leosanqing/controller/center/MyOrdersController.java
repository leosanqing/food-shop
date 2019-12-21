package com.leosanqing.controller.center;

import com.leosanqing.pojo.Users;
import com.leosanqing.service.center.CenterUserService;
import com.leosanqing.service.center.MyOrdersService;
import com.leosanqing.utils.JSONResult;
import com.leosanqing.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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








}
