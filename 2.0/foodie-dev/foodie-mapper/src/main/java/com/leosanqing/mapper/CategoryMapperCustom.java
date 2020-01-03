package com.leosanqing.mapper;

import com.leosanqing.pojo.vo.CategoryVO;
import com.leosanqing.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: leosanqing
 * @Date: 2019-12-08 08:40
 */
public interface CategoryMapperCustom {
    /**
     * 查询子分类
     * @param rootCatId
     * @return
     */
    List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 查询每个分类下的六个商品信息
     * @param map
     * @return
     */
    List<NewItemsVO> getSixNewItemsLazy(@Param("paramMap") Map<String,Object> map);
}
