package com.leosanqing.pojo.vo;

import lombok.Data;

/**
 * @Author: leosanqing
 * @Date: 2019-12-13 01:28
 */

@Data
public class ShopcartVO {
    private String itemId;
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    private String priceDiscount;
    private String priceNormal;
}
