package com.jzue.study.redis;

import lombok.Data;
import lombok.ToString;

/**
 * @Author: junzexue
 * @Date: 2019/5/9 下午4:36
 * @Description:
 **/
public class User {
    private String uname;
    private String pword;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPword() {
        return pword;
    }

    public void setPword(String pword) {
        this.pword = pword;
    }

    @Override
    public String toString() {
        return "User{" +
                "uname='" + uname + '\'' +
                ", pword='" + pword + '\'' +
                '}';
    }

    //事实证明java中对象的话，只有new才会产生新对象，直接把对象赋给另一个引用，相当于有两个引用（user/user1）同时指向user这个对象
    public static void main(String[] args) {
        User user = new User();
        user.setUname("uu");
        user.setPword("pp");
        System.out.println(user.toString());
        User user1 = user;
        user1.setPword("p1p1");
        System.out.println(user.toString());
    }

}
