package com.jzue.msauth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: junzexue
 * @Date: 2019/4/8 上午8:32
 * @Description:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private int statusCode;
    private String statusMessage;
    private T data;

    public CommonResponse(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
