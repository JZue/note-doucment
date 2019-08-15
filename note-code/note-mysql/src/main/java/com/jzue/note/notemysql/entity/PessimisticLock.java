package com.jzue.note.notemysql.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: junzexue
 * @Date: 2019/8/15 下午2:01
 * @Description:悲观锁操作实体
 **/
@Table(name = "pessimistic_lock")
@Entity
@Data
public class PessimisticLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "count")
    private Integer count;
}
