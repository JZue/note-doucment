package com.jzue.msauth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: junzexue
 * @Date: 2019/4/8 上午8:46
 * @Description:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private String accessToken;
    private String refreshToken;
}
