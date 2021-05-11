package com.jzue.msauth.service;

import com.jzue.msauth.dao.UserDao;
import com.jzue.msauth.entity.User;
import com.jzue.msauth.exception.UserCheckException;
import com.jzue.msauth.exception.UserNotExistException;
import com.jzue.msauth.utils.MessageHandler;
import com.jzue.msauth.vo.Token;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @Author: junzexue
 * @Date: 2019/4/8 上午9:43
 * @Description:
 **/
@Service
public class UserService {
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private UserDao userDao;


    /**
     * 用户名+密码登陆
     **/
    public Token unameAndPwordlogin(String uname,String pword,String clientKey,String serverKey){
        // todo: 校验登陆用户信息
        if (MessageHandler.checkPassWord(pword)&&MessageHandler.checkUserName(uname)){
            String  token=null;
            pword=MessageHandler.getMd5Message(pword,uname.substring(1));
            uname=MessageHandler.getMd5Message(uname,pword.substring(1));
            List<User> userList=userDao.findByUNameAndPWord(uname,pword);
            if (userList.size()==1){
                token= MessageHandler.getTokenUtil(uname,pword,new Date(),clientKey,serverKey,new Random().nextInt(),new Random().nextInt());
            } else {
                throw new UserNotExistException("账号或密码输入有误，请重新输入");
            }
            return new Token(token,token);
        } else {
            throw new UserCheckException("用户登陆信息格式有误");
        }
    }


    public boolean register(User user){
        Boolean status=MessageHandler.checkUserInfo(user);
        if (status){
            user.setUName(MessageHandler.getMd5Message(user.getUName(),user.getPWord()));
            user.setPWord(MessageHandler.getMd5Message(user.getPWord(),user.getUName()));
            userDao.save(user);
        }
        return false;
    }
}
