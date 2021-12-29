package com.jzue.note.notemysql.entity;

import javax.persistence.*;

/**
 * @Author: junzexue
 * @Date: 2019/8/15 上午9:00
 * @Description:乐观锁demo实体,此处直接就只用的是JPA的@Version注解(偷懒啦~~~(*╹▽╹*))
 **/
@Table(name = "optimistic_lock")
@Entity
public class OptimisticLock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "count")
    private Integer count;

    @Column(name = "version")
    @Version
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
