package com.leosanqing.service;

import com.leosanqing.pojo.Users;
import com.leosanqing.pojo.bo.UserBO;

/**
 * @Author: leosanqing
 * @Date: 2019-12-06 00:14
 */

public interface UserService {

    /**
     * 查询用户名是否存在
     *
     * @param username
     * @return
     */
    boolean queryUsernameIsExist(String username);


    /**
     * 创建用户
     *
     * @param userBO
     * @return
     */
    Users createUser(UserBO userBO);

    /**
     * 根据用户名和密码查询用户，登录时使用
     * @param username
     * @param password
     * @return
     */
    Users queryUsersForLogin(String username, String password);

}
