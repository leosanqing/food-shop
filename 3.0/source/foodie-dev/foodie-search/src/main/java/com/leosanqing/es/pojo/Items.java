package com.leosanqing.es.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Author: leosanqing
 * @Date: 2020/3/4 下午5:30
 * @Package: com.leosanqing.es.pojo
 * @Description: 商品实体类
 */
@Data
@Document(indexName = "foodie-items-ik",type = "doc")
public class Items {
    @Id
    @Field(store = true,type = FieldType.Text,index = false)
    private String id;

    @Field(store = true,type = FieldType.Text)
    private String itemName;

    @Field(store = true,type = FieldType.Text,index = false)
    private String imgUrl;

    @Field(store = true,type = FieldType.Integer)
    private Integer price;

    @Field(store = true,type = FieldType.Integer)
    private Integer sellCounts;



}
