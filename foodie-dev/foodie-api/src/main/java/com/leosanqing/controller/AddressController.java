package com.leosanqing.controller;

import com.leosanqing.pojo.UserAddress;
import com.leosanqing.pojo.bo.AddressBO;
import com.leosanqing.pojo.bo.ShopCartBO;
import com.leosanqing.service.AddressService;
import com.leosanqing.utils.JSONResult;
import com.leosanqing.utils.MobileEmailUtils;
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
@Api(value = "地址相关接口api", tags = {"查询地址相关"})
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping("list")
    @ApiOperation(value = "查询所有收货地址", notes = "查询所有收货地址", httpMethod = "POST")
    public JSONResult queryAll(
            @ApiParam(name = "userId", value = "用户id")
            @RequestParam String userId
    ) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户名id为空");
        }

        List<UserAddress> userAddresses = addressService.queryAll(userId);
        return JSONResult.ok(userAddresses);
    }


    @PostMapping("add")
    @ApiOperation(value = "添加收货地址", notes = "添加收货地址", httpMethod = "POST")
    public JSONResult add(
            @ApiParam(name = "addressBO", value = "收货地址BO")
            @RequestBody AddressBO addressBO
    ) {
        if (addressBO == null) {
            return JSONResult.errorMsg("传入地址对象为空");
        }

        JSONResult checkAddress = checkAddress(addressBO);
        if (200 != checkAddress.getStatus()) {
            return checkAddress;
        }
        addressService.addNewUserAddress(addressBO);
        return JSONResult.ok();
    }


    @PostMapping("update")
    @ApiOperation(value = "添加收货地址", notes = "添加收货地址", httpMethod = "POST")
    public JSONResult update(
            @ApiParam(name = "addressBO", value = "收货地址BO")
            @RequestBody AddressBO addressBO
    ) {
        if (addressBO == null) {
            return JSONResult.errorMsg("传入地址对象为空");
        }
        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return JSONResult.errorMsg("修改地址错误: 地址ID不能为空");
        }

        JSONResult checkAddress = checkAddress(addressBO);
        if (200 != checkAddress.getStatus()) {
            return checkAddress;
        }
        addressService.updateUserAddress(addressBO);
        return JSONResult.ok();
    }


    @PostMapping("delete")
    @ApiOperation(value = "删除收货地址", notes = "删除收货地址", httpMethod = "POST")
    public JSONResult del(
            @ApiParam(name = "userId", value = "用户Id")
            @RequestParam String userId,
            @ApiParam(name = "addressId", value = "收货地址Id")
            @RequestParam String addressId
    ) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return JSONResult.errorMsg("收货地址id 或 用户id 为空");
        }


        addressService.deleteUserAddress(userId, addressId);
        return JSONResult.ok();
    }



    @PostMapping("setDefault")
    @ApiOperation(value = "删除收货地址", notes = "删除收货地址", httpMethod = "POST")
    public JSONResult setDefault(
            @ApiParam(name = "userId", value = "用户Id")
            @RequestParam String userId,
            @ApiParam(name = "addressId", value = "收货地址Id")
            @RequestParam String addressId
    ) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return JSONResult.errorMsg("收货地址id 或 用户id 为空");
        }


        addressService.updateToBeDefault(userId, addressId);
        return JSONResult.ok();
    }

    /**
     * 校验地址信息是否规范
     *
     * @param addressBO
     * @return
     */
    private JSONResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return JSONResult.errorMsg("收货人不能为空");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return JSONResult.errorMsg("手机号不能为空");
        }
        if (mobile.length() > 11) {
            return JSONResult.errorMsg("手机号超过11位");
        }
        if (!MobileEmailUtils.checkMobileIsOk(mobile)) {
            return JSONResult.errorMsg("手机号不符合规范");
        }

        if (StringUtils.isBlank(addressBO.getCity())
                || StringUtils.isBlank(addressBO.getProvince())
                || StringUtils.isBlank(addressBO.getDetail())
                || StringUtils.isBlank(addressBO.getDistrict())) {
            return JSONResult.errorMsg("收货信息不能为空");
        }
        return JSONResult.ok();
    }


}
