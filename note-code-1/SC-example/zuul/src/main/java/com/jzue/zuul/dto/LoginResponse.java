package com.jzue.zuul.dto;

import lombok.Data;

/**
 * @Author: junzexue
 * @Date: 2019/4/8 上午8:32
 * @Description:
 **/
@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;

}
