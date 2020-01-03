package com.leosanqing.service;

import com.leosanqing.pojo.Carousel;

import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-07 22:35
 */
public interface CarouselService {
    /**
     * 查询所有需要播放的图
     * @param isShow
     * @return
     */
    List<Carousel> queryAll(Integer isShow);
}
