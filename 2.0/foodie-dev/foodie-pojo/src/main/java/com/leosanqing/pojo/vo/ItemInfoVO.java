package com.leosanqing.pojo.vo;

import com.leosanqing.pojo.Items;
import com.leosanqing.pojo.ItemsImg;
import com.leosanqing.pojo.ItemsParam;
import com.leosanqing.pojo.ItemsSpec;
import lombok.Data;

import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-08 21:10
 *
 * 商品详情VO
 */
@Data
public class ItemInfoVO {

    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;
}
