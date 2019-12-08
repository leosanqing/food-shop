package com.leosanqing.service.impl;

import com.leosanqing.enums.CommentLevel;
import com.leosanqing.mapper.*;
import com.leosanqing.pojo.*;
import com.leosanqing.pojo.vo.CommentLevelCountsVO;
import com.leosanqing.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2019-12-08 20:49
 */
@Service
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

    private Integer getCommentCounts(String itemId, Integer level) {
        ItemsComments comments = new ItemsComments();
        comments.setCommentLevel(level);
        comments.setItemId(itemId);
        return itemsCommentsMapper.selectCount(comments);
    }
}
