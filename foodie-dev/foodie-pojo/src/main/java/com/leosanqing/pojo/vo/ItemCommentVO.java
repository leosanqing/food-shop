package com.leosanqing.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author: leosanqing
 * @Date: 2019-12-08 23:05
 *
 * 展示商品评价
 */
@Data
public class ItemCommentVO {
    private Integer commentLevel;
    private String content;
    private String specName;
    private Date createdTime;
    private String userFace;
    private String nikename;
}
