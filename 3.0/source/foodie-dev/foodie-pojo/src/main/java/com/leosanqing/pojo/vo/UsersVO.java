package com.leosanqing.pojo.vo;

import lombok.Data;

/**
 * @Author: leosanqing
 * @Date: 2020/2/17 下午1:10
 * @Package: com.leosanqing.pojo.vo
 * @Description: 携带token的用户对象
 */
@Data
public class UsersVO {
    private String id;
    private String username;
    private String nickname;
    private String face;
    /**
     * 0 表示女，1 表示男 2表示保密
     */
    private int sex;

    private String userUniqueToken;

}
