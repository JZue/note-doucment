package com.jzue.msauth.constant;

/**
 * @Author: junzexue
 * @Date: 2019/4/8 上午9:20
 * @Description:
 **/
public class Constant {
    public static final int SUCCESS_CODE=0;
    public static final String SUCCESS_MESSAGE="请求成功";
    public static final String SUCCESS_LOGIN_MESSAGE="Dear user: your account login success";
    public static final int UNAUTHORIZED_CODE=401;
    public static final String UNAUTHORIZED_MESSAGE="请求认证鉴权失败";
    public static final int INNER_ERROR_CODE=1001;
    public static final String  INNER_ERROR_MESSAGE="服务器内部错误";
    public static final int USER_CHECK_ERROR_CODE=100000001;
    public static final String USER_CHECK_ERROR_MESSAGE="用户登录信息格式有误";
}
