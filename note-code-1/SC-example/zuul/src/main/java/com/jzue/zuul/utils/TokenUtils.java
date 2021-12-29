package com.jzue.zuul.utils;

import com.jzue.zuul.dto.User;
import com.jzue.zuul.exception.NullTokenException;


/**
 * @Author: junzexue
 * @Date: 2019/4/8 上午8:25
 * @Description:
 **/
public class TokenUtils {

    public User checkToken(String token){
        if (token.isEmpty()){
            throw new NullTokenException();
        }
        User user=new User();
        user.setUsername("demo");
        user.setPassword("test access");
        return user;
    }
}
