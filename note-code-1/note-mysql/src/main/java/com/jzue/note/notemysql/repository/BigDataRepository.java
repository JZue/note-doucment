package com.jzue.note.notemysql.repository;

import com.jzue.note.notemysql.entity.BigData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: junzexue
 * @Date: 2019/8/19 上午10:19
 * @Description:
 **/
public interface BigDataRepository extends JpaRepository<BigData,Long>, JpaSpecificationExecutor<BigData> {
    @Query(value = "insert into big_data(id,big_decimal,char_type,date_time,double_num,float_num,int_num,text,varchar_type) values(?1,?2,?3,?4,?5,?6,?7,?8,?9) ",nativeQuery = true)
    @Modifying
    public void insert(Long id, BigDecimal bigDecimal, String charType, Date dateTime,Double doubleNum,Float floatNum,int intNum,String text,String varcharType);
}

