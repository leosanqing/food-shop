package com.leosanqing.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-08 08:53
 */
@Data
public class CategoryVO {
    private Integer id;
    private String name;
    private String type;
    private Integer fatherId;
    private List<SubCategoryVO> subCatList;
}
