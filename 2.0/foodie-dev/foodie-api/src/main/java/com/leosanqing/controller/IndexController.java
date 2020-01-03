package com.leosanqing.controller;

import com.leosanqing.enums.YesOrNo;
import com.leosanqing.pojo.Carousel;
import com.leosanqing.pojo.Category;
import com.leosanqing.pojo.vo.CategoryVO;
import com.leosanqing.pojo.vo.NewItemsVO;
import com.leosanqing.service.CarouselService;
import com.leosanqing.service.CategoryService;
import com.leosanqing.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-07 22:42
 */
@RestController
@RequestMapping("index")
@Api(value = "首页", tags = {"首页展示的相关接口"})
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("carousel")
    @ApiOperation(value = "获取首页了轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    public JSONResult carousel() {
        List<Carousel> carousels = carouselService.queryAll(YesOrNo.YES.type);

        return JSONResult.ok(carousels);
    }

    @GetMapping("cats")
    @ApiOperation(value = "获取一级目录所有节点", notes = "获取一级目录所有节点", httpMethod = "GET")
    public JSONResult cats() {

        List<Category> categories = categoryService.queryAllRootLevelCat();
        return JSONResult.ok(categories);
    }

    @GetMapping("subCat/{rootCatId}")
    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    public JSONResult subCats(
            @ApiParam(name = "rootCatId", value = "一级分类Id", required = true)
            @PathVariable Integer rootCatId) {


        if (rootCatId == null) {
            return JSONResult.errorMsg("商品分类不存在");
        }
        List<CategoryVO> categories = categoryService.getSubCatList(rootCatId);
        return JSONResult.ok(categories);
    }


    @GetMapping("sixNewItems/{rootCatId}")
    @ApiOperation(value = "查询每个分类下的六个最新商品", notes = "查询每个分类下的六个最新商品", httpMethod = "GET")
    public JSONResult getSixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类Id", required = true)
            @PathVariable Integer rootCatId) {


        if (rootCatId == null) {
            return JSONResult.errorMsg("商品分类不存在");
        }
        List<NewItemsVO> categories = categoryService.getSixNewItemsLazy(rootCatId);
        return JSONResult.ok(categories);
    }


}
