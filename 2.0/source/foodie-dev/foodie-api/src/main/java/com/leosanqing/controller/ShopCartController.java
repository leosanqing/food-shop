package com.leosanqing.controller;

import com.leosanqing.pojo.bo.ShopCartBO;
import com.leosanqing.utils.JSONResult;
import com.leosanqing.utils.JsonUtils;
import com.leosanqing.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author: leosanqing
 * @Date: 2019-12-12 07:59
 */
@RestController
@RequestMapping("shopcart")
@Api(value = "购物车相关接口api", tags = {"用于购物车相关操作"})
public class ShopCartController extends BaseController{

    @Autowired
    private RedisOperator redisOperator;

    @PostMapping("add")
    @ApiOperation(value = "添加购物车", notes = "添加购物车", httpMethod = "POST")
    public JSONResult add(
            @ApiParam(name = "userId", value = "用户id")
            @RequestParam String userId,
            @ApiParam(name = "shopCartBO", value = "从前端传来的购物车对象")
            @RequestBody ShopCartBO shopCartBO
//            HttpServletRequest request,
//            HttpServletResponse response
    ) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户名id为空");
        }


        System.out.println(shopCartBO);
        // 前端用户在登录情况下，添加商品到购物车，会同步数据到redis

        List<ShopCartBO> shopCartBOList;
        final String shopCartStr = redisOperator.get(SHOP_CART + ":" + userId);
        if (StringUtils.isBlank(shopCartStr)) {
            shopCartBOList = new ArrayList<>();
            shopCartBOList.add(shopCartBO);

        } else {
            shopCartBOList = JsonUtils.jsonToList(shopCartStr, ShopCartBO.class);

            boolean isExist = false;

            for (ShopCartBO cartBO : Objects.requireNonNull(shopCartBOList)) {
                final String specId = cartBO.getSpecId();
                if (specId.equals(shopCartBO.getSpecId())) {
                    cartBO.setBuyCounts(cartBO.getBuyCounts() + shopCartBO.getBuyCounts());
                    isExist = true;
                }
                if (!isExist) {
                    shopCartBOList.add(shopCartBO);
                }
            }
        }

        redisOperator.set(SHOP_CART, JsonUtils.objectToJson(shopCartBOList));

        return JSONResult.ok();
    }


    @PostMapping("del")
    @ApiOperation(value = "删除购物车", notes = "删除购物车", httpMethod = "POST")
    public JSONResult del(
            @ApiParam(name = "userId", value = "用户id")
            @RequestParam String userId,
            @ApiParam(name = "itemSpecId", value = "购物车中的商品规格")
            @RequestBody String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return JSONResult.errorMsg("参数不能为空");
        }


        //  前端用户在登录情况下，删除商品到购物车，会同步数据到redis
        final String shopCartStr = redisOperator.get(SHOP_CART + ":" + userId);
        if (StringUtils.isNotBlank(shopCartStr)) {
            final List<ShopCartBO> shopCartBOList = JsonUtils.jsonToList(shopCartStr, ShopCartBO.class);
            if (shopCartBOList != null) {
                for (ShopCartBO cartBO : shopCartBOList) {
                    if (cartBO.getSpecId().equals(itemSpecId)) {
                        shopCartBOList.remove(cartBO);
                        break;
                    }
                }
            }
            redisOperator.set(SHOP_CART + ":" + userId, JsonUtils.objectToJson(shopCartBOList));
        }

        return JSONResult.ok();
    }
}
