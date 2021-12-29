package com.jzue.note.notemysql.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: junzexue
 * @Date: 2019/8/17 上午9:32
 * @Description: 此表主要是大数据量下的问题的模拟
 **/
@Table(name = "big_data")
@Entity
@Data
public class BigData {

    @Id
    private Long id;

    @Column(name = "big_decimal")
    private BigDecimal bigDecimal;

    @Column(name = "int_num")
    private Integer intNum;

    @Column(name = "float_num")
    private Float floatNum;

    @Column(name = "double_num")
    private Double doubleNum;

    @Column(name = "varchar_type")
    private String varcharType;

    /**
     * char(10) 类型定长，无论字符串长度是多少，都会占用10个字节
     **/
    @Column(name = "char_type",columnDefinition = "char(10) not null")
    private String charType;

    @Column(name = "text",columnDefinition = "TEXT not null")
    private String text;

    @Column(name = "date_time")
    private Date dateTime;


}
