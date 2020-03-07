package com.leosanqing.service.impl;

import com.leosanqing.es.pojo.Items;
import com.leosanqing.service.ItemESService;
import com.leosanqing.utils.PagedGridResult;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: leosanqing
 * @Date: 2020/3/4 下午5:55
 * @Package: com.leosanqing.service
 * @Description: 商品搜索实现类
 */
@Service
public class ItemESServiceImpl implements ItemESService {

    @Autowired
    private ElasticsearchTemplate template;

    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {

        String preTag = "<font color='red'>";
        String postTag = "</font>";

        String itemNameField = "itemName";

        Pageable pageable = PageRequest.of(page, pageSize);

        FieldSortBuilder order = null;

        if ("c".equals(sort)) {
            order = new FieldSortBuilder("sellCounts").order(SortOrder.DESC);
        } else if ("p".equals(sort)) {
            order = new FieldSortBuilder("price").order(SortOrder.ASC);
        } else {
            order = new FieldSortBuilder("itemName.keyword").order(SortOrder.DESC);

        }

        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery(itemNameField, keywords))
                .withHighlightFields(new HighlightBuilder.Field(itemNameField)
//                        .preTags(preTag).postTags(postTag)
                )
                .withSort(order)
                .withPageable(pageable)
                .build();

        AggregatedPage<Items> itemsAggregatedPage = template.queryForPage(build, Items.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<Items> list = new ArrayList<>();
                SearchHits hits = response.getHits();
                for (SearchHit hit : hits) {
                    HighlightField highlightField = hit.getHighlightFields().get(itemNameField);
                    String itemsName = highlightField.getFragments()[0].toString();

                    String id = (String) hit.getSourceAsMap().get("id");
                    String imgUrl = (String) hit.getSourceAsMap().get("imgUrl");
                    Integer price = (Integer) hit.getSourceAsMap().get("price");
                    Integer sellCounts = (Integer) hit.getSourceAsMap().get("sellCounts");


                    Items items = new Items();

                    items.setId(id);
                    items.setImgUrl(imgUrl);
                    items.setItemName(itemsName);
                    items.setPrice(price);
                    items.setSellCounts(sellCounts);


                    list.add(items);
                }

                return new AggregatedPageImpl<>((List<T>) list,
                        pageable, response.getHits().totalHits);
            }
        });

        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setRows(itemsAggregatedPage.getContent());
        pagedGridResult.setPage(page + 1);
        pagedGridResult.setTotal(itemsAggregatedPage.getTotalPages());
        pagedGridResult.setRecords(itemsAggregatedPage.getTotalElements());
        return pagedGridResult;
    }


}
