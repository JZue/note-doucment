package com.jzue;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @Author: junzexue
 * @Date: 2019/6/13 上午9:12
 * @Description:
 **/
public class DemoMain {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String text = "%E9%94%84%E7%A6%BE%E6%97%A5%E5%BD%93%E5%8D%88%EF%BC%8C%E6%B1%97%E6%BB%B4%E7%A6%BE%E4%B8%8B%E5%9C%9F%EF%BC%8C%E8%B0%81%E7%9F%A5%E7%9B%98%E4%B8%AD%E9%A4%90%EF%BC%8C%E7%B2%92%E7%B2%92%E7%9A%86%E8%BE%9B%E8%8B%A6%7C";
        String s=URLDecoder.decode(text,"UTF-8");
        System.out.println(s);
    }
    public static String getUnicode(String source) {
        String result = "";

        for (int i = 0; i < source.length(); i++) {
            result += "\\u"+Integer.toHexString((int) source.charAt(i));
        }

        return result;
    }
}
