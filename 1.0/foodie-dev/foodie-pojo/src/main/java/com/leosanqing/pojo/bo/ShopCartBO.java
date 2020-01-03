package com.leosanqing.pojo.bo;

import lombok.Data;

/**
 * @Author: leosanqing
 * @Date: 2019-12-12 08:07
 */
@Data
public class ShopCartBO {
    private String itemId;
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    private Integer buyCounts;
    private String priceDiscount;
    private String priceNormal;

}
