package com.leosanqing.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-08 08:53
 */
@Data
public class NewItemsVO {
    private Integer rootCatId;
    private String rootCatName;
    private String slogan;
    private String catImage;
    private String bgColor;

    private List<SimpleItemVO> simpleItemList;
}
