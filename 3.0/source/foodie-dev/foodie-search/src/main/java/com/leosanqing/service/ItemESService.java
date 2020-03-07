package com.leosanqing.service;

import com.leosanqing.utils.PagedGridResult;

/**
 * @Author: leosanqing
 * @Date: 2020/3/4 下午5:53
 * @Package: com.leosanqing.service
 * @Description: 商品搜索服务接口
 */
public interface ItemESService {
    PagedGridResult searchItems(String keywords,String sort,
                                Integer page,Integer pageSize);
}
