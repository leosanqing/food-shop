package com.leosanqing.mapper;

import com.leosanqing.my.mapper.MyMapper;
import com.leosanqing.pojo.Items;
import com.leosanqing.pojo.ItemsComments;
import com.leosanqing.pojo.vo.ItemCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 自定义的商品Mapper ，用于查询评价
 */
public interface ItemsMapperCustom {

    /**
     * 查询商品评价
     *
     * @return
     */
    List<ItemCommentVO> queryItemComments(
            @Param("paramsMap") Map<String, Object> map);
}