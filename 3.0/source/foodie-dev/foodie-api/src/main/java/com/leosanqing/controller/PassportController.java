package com.leosanqing.controller;

import com.leosanqing.pojo.Users;
import com.leosanqing.pojo.bo.UserBO;
import com.leosanqing.pojo.vo.UsersVO;
import com.leosanqing.service.UserService;
import com.leosanqing.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Author: leosanqing
 * @Date: 2019-12-06 00:22
 */
@RestController
@RequestMapping("passport")
@Api(value = "注册登录", tags = {"用于注册的接口"})
@Slf4j
public class PassportController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;


    @GetMapping("usernameIsExist")
    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    public JSONResult usernameIsExist(@RequestParam String username) {

        InputMDC.inputMDC();
        log.info("这是条info信息");
        log.warn("这是条warn信息");
        log.error("这是条error信息");
        MDC.put("0001","234");

        // 判断用户名为空
        if (StringUtils.isBlank(username)) {

            return JSONResult.errorMsg("用户名不能为空");
        }

        // 判断用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return JSONResult.errorMsg("用户名已存在");
        }

        return JSONResult.ok();
    }

    @PostMapping("regist")
    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    public JSONResult register(@RequestBody UserBO userBO,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();


        // 判断用户名密码为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)
                || StringUtils.isBlank(confirmPassword)) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }

        // 查询用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return JSONResult.errorMsg("用户名已存在");
        }

        // 判断密码长度不能短于6
        if (password.length() < 6) {
            return JSONResult.errorMsg("密码长度不能小于6");
        }

        // 判断两次密码是否一致
        if (!password.equals(confirmPassword)) {
            return JSONResult.errorMsg("两次密码不一致");
        }

        // 实现注册
        Users users = userService.createUser(userBO);


//        Users userResult = setNullProperty(users);

        // 生成token，用于分布式会话
        UsersVO usersVO = convertUsersVO(users);

        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(usersVO), true);

        // 同步数据到redis
        syncShopCartData(users.getId(),request,response);

        return JSONResult.ok();

    }



    @PostMapping("login")
    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    public JSONResult login(@RequestBody UserBO userBO,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();


        // 判断用户名密码为空
        if (StringUtils.isBlank(username)
                || StringUtils.isBlank(password)) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }

        // 查询用户名是否存在
        Users users = null;
        try {
            users = userService.queryUsersForLogin(username,
                    MD5Utils.getMD5Str(password));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (users == null) {
            return JSONResult.errorMsg("用户名或密码不正确");
        }


        // 分布式会话
        UsersVO usersVO = convertUsersVO(users);

        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(usersVO), true);
        // 实现登录


        //  同步数据到redis
        syncShopCartData(usersVO.getId(),request,response);
        return JSONResult.ok(usersVO);

    }


    /**
     * 同步购物车数据
     * @param userId
     * @param request
     * @param response
     */
    private void syncShopCartData(String userId,HttpServletRequest request,
                                  HttpServletResponse response ){
        /**
         *  1. redis 为空，cookie 也为空。
         *                cookie 不为空，将 cookie的数据直接存入redis
         *  2. redis 不为空，cookie 为空，将redis数据覆盖cookie数据
         *                  cookie 不为空，如果存在相同商品，以cookie为主
         *
         */

        final String shopCartRedisStr = redisOperator.get(SHOP_CART + ":" + userId);
        final String cookieValue = CookieUtils.getCookieValue(request, SHOP_CART, true);

        // redis为为空，cookie不为空
        if(StringUtils.isBlank(shopCartRedisStr)){

            if(StringUtils.isNotBlank(cookieValue)){
                redisOperator.set(SHOP_CART+":"+userId,cookieValue);
            }

        }else{
            if(StringUtils.isNotBlank(cookieValue)){
            }else {
                CookieUtils.setCookie(request,response,SHOP_CART,shopCartRedisStr,true);
            }
        }
    }

    /**
     * 将用户的部分信息设置为空，保护隐私
     *
     * @param users
     * @return
     */
    private Users setNullProperty(Users users) {
        users.setUpdatedTime(null);
        users.setCreatedTime(null);
        users.setBirthday(null);
        users.setMobile(null);
        users.setRealname(null);
        users.setEmail(null);
        users.setPassword(null);

        return users;
    }


    @PostMapping("logout")
    @ApiOperation(value = "退出登录", notes = "退出登录", httpMethod = "POST")
    public JSONResult logout(@RequestParam String userId,
                             HttpServletRequest request,
                             HttpServletResponse response) {


        CookieUtils.deleteCookie(request, response, "user");

        // 清除redis数据
        redisOperator.del(REDIS_USER_TOKEN+":"+userId);

        return JSONResult.ok();

    }


}
