package com.jzue.note.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * @Author: junzexue
 * @Date: 2019/7/19 上午12:49
 * @Description:
 **/
@Document(indexName = "xueJunZe",type = "book")
@Setter@Getter
public class Book {
    @Id
    @Field
    private Long id;

    @Field
    private String bookName;
}
