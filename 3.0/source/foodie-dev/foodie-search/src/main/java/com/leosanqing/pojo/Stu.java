package com.leosanqing.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * @Author: leosanqing
 * @Date: 2020/3/3 下午11:39
 * @Package: com.leosanqing.pojo
 * @Description: 学生类
 */

@Data
@Document(indexName = "stu", type = "_doc")
public class Stu {

    @Id
    private Long stuId;

    @Field(store = true)
    private String name;

    @Field(store = true )
    private int age;


}
