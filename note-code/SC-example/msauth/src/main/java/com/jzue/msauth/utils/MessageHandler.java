package com.jzue.msauth.utils;

import com.jzue.msauth.entity.User;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @Author: junzexue
 * @Date: 2019/4/8 上午9:56
 * @Description:
 **/
public class MessageHandler {

    public static String getMd5Message(String message,String salt){
        String secritMessage=message;
        return secritMessage;
    }
    /**
     * @Param  uname 用户名(MD5)
     * @Param  pword 密码(MD5)
     * @Param  timeStamp 时间系数
     * @Param  tokenClientKey 客户端key
     * @Param  tokenServerKey 服务端key
     * @Param  randomNum 随机数
     * @Param  shiftAmount 偏移量
     **/
    public static String getTokenUtil(String uname, String pword, Date timeStamp,
                                      String tokenClientKey,String tokenServerKey,
                                      int randomNum,int shiftAmount){

        return UUID.randomUUID().toString();
    }
    public static boolean checkUserName(String uname){
        return true;
    }
    public static boolean checkPassWord(String pword){
        return true;
    }

    public static boolean checkUserInfo(User user){
        return true;
    }
}
