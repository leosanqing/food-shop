package com.leosanqing.service.impl;

import com.leosanqing.mapper.CarouselMapper;
import com.leosanqing.pojo.Carousel;
import com.leosanqing.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-07 22:37
 */
@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Carousel> queryAll(Integer isShow) {

        Example example = new Example(Carousel.class);
        example.orderBy("sort");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow",isShow);

        List<Carousel> carousels = carouselMapper.selectByExample(example);


        return carousels;
    }
}
