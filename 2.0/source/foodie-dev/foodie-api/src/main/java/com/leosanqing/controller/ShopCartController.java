package com.leosanqing.controller;

import com.leosanqing.pojo.bo.ShopCartBO;
import com.leosanqing.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: leosanqing
 * @Date: 2019-12-12 07:59
 */
@RestController
@RequestMapping("shopcart")
@Api(value = "购物车相关接口api",tags = {"用于购物车相关操作"})
public class ShopCartController {

    @PostMapping("add")
    @ApiOperation(value = "添加购物车",notes ="添加购物车",httpMethod = "POST")
    public JSONResult add(
            @ApiParam(name = "userId",value = "用户id")
            @RequestParam String userId,
            @ApiParam(name = "shopCartBO",value = "从前端传来的购物车对象")
            @RequestBody ShopCartBO shopCartBO
//            HttpServletRequest request,
//            HttpServletResponse response
    ){
        if(StringUtils.isBlank(userId)){
            return JSONResult.errorMsg("用户名id为空");
        }


        System.out.println(shopCartBO);
        // TODO 前端用户在登录情况下，添加商品到购物车，会同步数据到redis
        return JSONResult.ok();
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
