package com.leosanqing.service.center;

import com.leosanqing.pojo.Users;
import com.leosanqing.pojo.bo.center.CenterUserBO;

/**
 * @Author: leosanqing
 * @Date: 2019-12-15 20:02
 * @Package: com.leosanqing.service.center
 * @Description: 用户中心service
 */
public interface CenterUserService {

    /**
     * 根据用户Id查询用户信息
     *
     * @param userId
     * @return
     */
    Users queryUserInfo(String userId);


    /**
     * 更新用户信息
     *
     * @param userId
     * @param centerUserBO
     * @return
     */
    Users updateUserInfo(String userId, CenterUserBO centerUserBO);


    /**
     * 更新用户头像
     * @param userId
     * @param faceUrl
     * @return
     */
    Users updateUserFace(String userId, String faceUrl);
}
