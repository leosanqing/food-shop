package com.leosanqing.mapper;

import com.leosanqing.my.mapper.MyMapper;
import com.leosanqing.pojo.Items;
import com.leosanqing.pojo.ItemsComments;
import com.leosanqing.pojo.bo.ShopCartBO;
import com.leosanqing.pojo.vo.ItemCommentVO;
import com.leosanqing.pojo.vo.SearchItemsVO;
import com.leosanqing.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 自定义的商品Mapper ，用于查询评价
 */
public interface ItemsMapperCustom {

    /**
     * 查询商品评价
     * @param map
     * @return
     */
    List<ItemCommentVO> queryItemComments(
            @Param("paramsMap") Map<String, Object> map);

    /**
     * 根据关键字查询商品
     * @param map
     * @return
     */
    List<SearchItemsVO> searchItems(
            @Param("paramsMap") Map<String, String> map);

    /**
     * 根据第三级目录查询商品
     * @param map
     * @return
     */
    List<SearchItemsVO> searchItemsByThirdCatId(
            @Param("paramsMap") Map<String, Object> map);


    /**
     * 根据第三级目录查询商品
     * @param specIdsList
     * @return
     */
    List<ShopcartVO> queryItemsBySpecIds(@Param("paramsList") List specIdsList);

    /**
     * 减库存
     * @param pendingCount
     * @param specId
     * @return
     */
    Integer decreaseItemSpecStock(@Param("itemSpecId") String specId,
                                  @Param("pendingCount") Integer pendingCount);


}