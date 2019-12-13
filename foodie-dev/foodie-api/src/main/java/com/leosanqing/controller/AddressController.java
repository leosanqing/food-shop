package com.leosanqing.controller;

import com.leosanqing.pojo.UserAddress;
import com.leosanqing.pojo.bo.ShopCartBO;
import com.leosanqing.service.AddressService;
import com.leosanqing.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-12 07:59
 */
@RestController
@RequestMapping("address")
@Api(value = "地址相关接口api",tags = {"查询地址相关"})
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping("list")
    @ApiOperation(value = "查询所有收货地址",notes ="查询所有收货地址",httpMethod = "POST")
    public JSONResult queryAll(
            @ApiParam(name = "userId",value = "用户id")
            @RequestParam String userId
    ){
        if(StringUtils.isBlank(userId)){
            return JSONResult.errorMsg("用户名id为空");
        }

        List<UserAddress> userAddresses = addressService.queryAll(userId);
        return JSONResult.ok(userAddresses);
    }



    @PostMapping("del")
    @ApiOperation(value = "删除购物车",notes ="删除购物车",httpMethod = "POST")
    public JSONResult del(
            @ApiParam(name = "userId",value = "用户id")
            @RequestParam String userId,
            @ApiParam(name = "itemSpecId",value = "购物车中的商品规格")
            @RequestBody String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)){
            return JSONResult.errorMsg("参数不能为空");
        }


        // TODO 前端用户在登录情况下，删除商品到购物车，会同步数据到redis
        return JSONResult.ok();
    }
}
