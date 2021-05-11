package com.jzue.msauth.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: junzexue
 * @Date: 2019/4/8 上午10:04
 * @Description:
 **/
@Entity
@Where(clause = "del_flag=1")
@Table(name = "auth_user")
@Data
public class User {

    @Id
    @GenericGenerator(name = "id",strategy = "uuid")
    @GeneratedValue(generator = "id")
    private String uid;

    private String uName;

    private String pWord;

    private String age;

    private boolean sex;

    private String email;

    private String mobile;

    private String address;

    private String avatar;

    @Column(name = "del_flag")
    private int delFlag;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;
}
