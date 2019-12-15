package com.leosanqing.service;

import com.leosanqing.pojo.Items;
import com.leosanqing.pojo.ItemsImg;
import com.leosanqing.pojo.ItemsParam;
import com.leosanqing.pojo.ItemsSpec;
import com.leosanqing.pojo.bo.ShopCartBO;
import com.leosanqing.pojo.vo.CommentLevelCountsVO;
import com.leosanqing.pojo.vo.ItemCommentVO;
import com.leosanqing.pojo.vo.ShopcartVO;
import com.leosanqing.utils.PagedGridResult;

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


    /**
     * 根据商品id查询商品评价
     * @param itemId
     * @param level
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * 根据关键字查询商品
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItems(String keywords,String sort,Integer page,Integer pageSize);

    /**
     * 根据第三级目录查询商品
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItemsByCatId(Integer catId,String sort,Integer page,Integer pageSize);

    /**
     * 根据商品规格id查询商品信息
     * @param specIds
     * @return
     */
    List<ShopcartVO> queryItemsBySpecIds(String specIds);

    /**
     * 根据商品id 查询商品规格
     * @param specId
     * @return
     */
    ItemsSpec queryItemBySpecId(String specId);


    /**
     * 根据商品id ，查询商品图片
     * @param itemId
     * @return
     */
    String queryItemImgByItemId(String itemId);

    /**
     * 减库存
     * @param specId
     * @param buyCount
     */
    void decreaseItemSpecStock(String specId,Integer buyCount);
}
