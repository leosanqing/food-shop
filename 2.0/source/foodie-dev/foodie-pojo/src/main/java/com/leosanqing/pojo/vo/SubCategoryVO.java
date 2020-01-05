package com.leosanqing.pojo.vo;

import lombok.Data;

/**
 * @Author: leosanqing
 * @Date: 2019-12-08 08:53
 */
@Data
public class SubCategoryVO {
    private Integer subId;
    private String subName;
    private String subType;
    private Integer subFatherId;
}
