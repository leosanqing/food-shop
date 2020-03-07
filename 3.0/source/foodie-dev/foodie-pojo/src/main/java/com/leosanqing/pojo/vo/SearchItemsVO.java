package com.leosanqing.pojo.vo;

import lombok.Data;

/**
 * @Author: leosanqing
 * @Date: 2019-12-11 07:56
 *
 * 搜索商品的VO
 */


@Data
public class SearchItemsVO {

    private String id;
    private String itemName;
    private Integer sellCounts;
    private String imgUrl;
    private Integer price;


}
