package com.leosanqing.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leosanqing.enums.CommentLevel;
import com.leosanqing.enums.YesOrNo;
import com.leosanqing.mapper.*;
import com.leosanqing.pojo.*;
import com.leosanqing.pojo.bo.ShopCartBO;
import com.leosanqing.pojo.vo.CommentLevelCountsVO;
import com.leosanqing.pojo.vo.ItemCommentVO;
import com.leosanqing.pojo.vo.SearchItemsVO;

import com.leosanqing.pojo.vo.ShopcartVO;
import com.leosanqing.service.ItemService;
import com.leosanqing.utils.DesensitizationUtil;
import com.leosanqing.utils.PagedGridResult;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: leosanqing
 * @Date: 2019-12-08 20:49
 */
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

    @Autowired
    private RedissonClient redisson;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemsById(String itemId) {

        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {

        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsParamMapper.selectOneByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        Integer good = getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normal = getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer bad = getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer total = good + bad + normal;

        CommentLevelCountsVO levelCountsVO = new CommentLevelCountsVO();
        levelCountsVO.setGoodCounts(good);
        levelCountsVO.setBadCounts(bad);
        levelCountsVO.setNormalCounts(normal);
        levelCountsVO.setTotalCounts(total);
        return levelCountsVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPagedComments(String itemId, Integer level,Integer page,Integer pageSize) {
        HashMap<String, Object> map = new HashMap<>(4);
        map.put("itemId", itemId);
        map.put("level", level);

        // 一定要写在sql执行之前，因为会对其进行拦截，加入自己的语句
        PageHelper.startPage(page,pageSize);
        List<ItemCommentVO> itemCommentVOS = itemsMapperCustom.queryItemComments(map);

        // 进行脱敏处理
        for (ItemCommentVO itemCommentVO : itemCommentVOS) {
            itemCommentVO.setNickname(DesensitizationUtil.commonDisplay(itemCommentVO.getNickname()));
        }

        return setterPage(itemCommentVOS,page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        HashMap<String, String> map = new HashMap<>();
        map.put("keywords",keywords);
        map.put("sort",sort);


        PageHelper.startPage(page,pageSize);
        List<SearchItemsVO> searchItemsVOS = itemsMapperCustom.searchItems(map);
        return setterPage(searchItemsVOS,page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult searchItemsByCatId(Integer catId, String sort, Integer page, Integer pageSize) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("catId",catId);
        map.put("sort",sort);


        PageHelper.startPage(page,pageSize);
        List<SearchItemsVO> searchItemsVOS = itemsMapperCustom.searchItemsByThirdCatId(map);
        return setterPage(searchItemsVOS,page);
    }

    private PagedGridResult setterPage(List<?> list,int page){
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }

    private Integer getCommentCounts(String itemId, Integer level) {
        ItemsComments comments = new ItemsComments();
        comments.setCommentLevel(level);
        comments.setItemId(itemId);
        return itemsCommentsMapper.selectCount(comments);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseItemSpecStock(String specId, Integer buyCount) {
        /**
         *  分布式锁【3】 编写业务代码
         *  1、Redisson是基于Redis，使用Redisson之前，项目必须使用Redis
         *   2、注意getLock方法中的参数，以specId作为参数，每个specId一个key，和
         *   数据库中的行锁是一致的，不会是方法级别的锁
         */
        RLock rLock = redisson.getLock("SPECID_"+specId);
        try {
            /**
             * 1、获取分布式锁，锁的超时时间是5秒get
             *  2、获取到了锁，进行后续的业务操作
             */
            rLock.lock(5, TimeUnit.HOURS);

            int result = itemsMapperCustom.decreaseItemSpecStock(specId, buyCount);
            if (result != 1) {
                throw new RuntimeException("订单创建失败，原因：库存不足!");
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(e.getMessage(),e);
        }finally {
            /**
             *  不管业务是否操作正确，随后都要释放掉分布式锁
             *   如果不释放，过了超时时间也会自动释放
             */
            rLock.unlock();
        }

//
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) {

        String ids[] = specIds.split(",");
        List<String> specIdsList = new ArrayList<>();
        Collections.addAll(specIdsList, ids);

        return itemsMapperCustom.queryItemsBySpecIds(specIdsList);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec queryItemBySpecId(String specId) {

        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String queryItemImgByItemId(String itemId) {
        final ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.type);
        final ItemsImg itemsImg1 = itemsImgMapper.selectOne(itemsImg);

        return itemsImg1 == null ? "" : itemsImg1.getUrl();
    }
}
