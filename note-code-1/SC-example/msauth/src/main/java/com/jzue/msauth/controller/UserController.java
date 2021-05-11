package com.jzue.msauth.controller;

import com.jzue.msauth.constant.Constant;
import com.jzue.msauth.entity.User;
import com.jzue.msauth.exception.UserCheckException;
import com.jzue.msauth.exception.UserNotExistException;
import com.jzue.msauth.service.UserService;
import com.jzue.msauth.vo.CommonResponse;
import com.jzue.msauth.vo.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: junzexue
 * @Date: 2018/12/28 下午2:08
 * @Description:
 **/
@RestController
@RequestMapping("/auth")
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;
    @Value("${serverKey}")
    private String serverKey;

    @GetMapping("/work")
    private String msTest(){
        return "service is working!!!!!";
    }


    @GetMapping("/login")
    private CommonResponse<Token> login(@RequestParam("username") String uname, @RequestParam("password") String pword,@RequestParam("clientKey") String clientKey){
        CommonResponse commonResponse = new CommonResponse();
        try {
            Token token=userService.unameAndPwordlogin(uname,pword,clientKey,serverKey);
            commonResponse.setStatusCode(Constant.SUCCESS_CODE);
            commonResponse.setStatusMessage(Constant.SUCCESS_LOGIN_MESSAGE);
            commonResponse.setData(token);
        }catch (UserNotExistException e){
            commonResponse.setStatusCode(Constant.UNAUTHORIZED_CODE);
            commonResponse.setStatusMessage(Constant.UNAUTHORIZED_MESSAGE);
        }catch (UserCheckException e){
            commonResponse.setStatusCode(Constant.USER_CHECK_ERROR_CODE);
            commonResponse.setStatusMessage(Constant.USER_CHECK_ERROR_MESSAGE);
        }catch (Exception e){
            commonResponse.setStatusCode(Constant.INNER_ERROR_CODE);
            commonResponse.setStatusMessage(Constant.INNER_ERROR_MESSAGE);
        }
        return commonResponse;
    }

    @PostMapping("register")
    public CommonResponse<Void> register(@RequestBody User user){
        CommonResponse<Void> response = new CommonResponse<Void>(Constant.SUCCESS_CODE,Constant.SUCCESS_MESSAGE);
        try {
            if(!userService.register(user)){
                response.setStatusCode(1000000001);
                response.setStatusMessage("注册失败，用户必填信息填写不完整或格式有误");
            }
        }catch (Exception e){
            response.setStatusCode(Constant.INNER_ERROR_CODE);
            response.setStatusMessage(Constant.INNER_ERROR_MESSAGE);
        }
        return response;
    }
}
