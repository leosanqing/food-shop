package com.leosanqing.service;

import com.leosanqing.pojo.Category;
import com.leosanqing.pojo.vo.CategoryVO;
import com.leosanqing.pojo.vo.NewItemsVO;

import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-07 23:14
 */
public interface CategoryService {

    /**
     * 查询一级分类下的所有节点
     * @return
     */
    List<Category> queryAllRootLevelCat();

    /**
     * 查询子分类信息
     * @param rootCatId
     * @return
     */
    List<CategoryVO> getSubCatList(Integer rootCatId);


    /**
     * 查询每个分类下的六个商品信息
     * @param rootCatId
     * @return
     */
    List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}
