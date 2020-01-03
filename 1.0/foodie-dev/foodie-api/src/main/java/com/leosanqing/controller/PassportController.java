package com.leosanqing.controller;

import com.leosanqing.pojo.Users;
import com.leosanqing.pojo.bo.UserBO;
import com.leosanqing.service.UserService;
import com.leosanqing.utils.CookieUtils;
import com.leosanqing.utils.JSONResult;
import com.leosanqing.utils.JsonUtils;
import com.leosanqing.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: leosanqing
 * @Date: 2019-12-06 00:22
 */
@RestController
@RequestMapping("passport")
@Api(value = "注册登录", tags = {"用于注册的接口"})
public class PassportController {

    @Autowired
    private UserService userService;


    @GetMapping("usernameIsExist")
    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    public JSONResult usernameIsExist(@RequestParam String username) {
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
        setNullProperty(users);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(users), true);


        // TODO 生成token，用于分布式会话
        // TODO 同步数据到redis
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

        users = setNullProperty(users);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(users), true);
        // 实现登录


        // TODO 生成token，用于分布式会话
        // TODO 同步数据到redis
        return JSONResult.ok(users);

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

        return JSONResult.ok();

    }


}
