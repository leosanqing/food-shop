package com.leosanqing.service;

import com.leosanqing.pojo.Items;
import com.leosanqing.pojo.ItemsImg;
import com.leosanqing.pojo.ItemsParam;
import com.leosanqing.pojo.ItemsSpec;
import com.leosanqing.pojo.vo.CommentLevelCountsVO;

import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-08 20:44
 */
public interface ItemService {

    /**
     * 根据商品id查询商品
     * @param itemId
     * @return
     */
    Items queryItemsById(String itemId);

    /**
     * 根据商品id查询商品图片
     * @param itemId
     * @return
     */
    List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id查询商品规格
     * @param itemId
     * @return
     */
    List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品Id查询商品参数
     * @param itemId
     * @return
     */
    ItemsParam queryItemParam(String itemId);


    /**
     * 查询商品的各个评价等级
     * @param itemId
     * @return
     */
    CommentLevelCountsVO queryCommentCounts(String itemId);
}
