package com.jzue.study.socket.udp;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * @Author: junzexue
 * @Date: 2019/2/22 上午11:51
 * @Description:
 **/
@Slf4j
public class Provider {
    private String x;
    public static void main(String[] args) {
        System.out.println(md5("17786349653"));
    }


    public static String md5(String text) {
        try {
            byte[] bts = text.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bts_hash = md.digest(bts);
            StringBuffer buf = new StringBuffer();
            for (byte b : bts_hash) {
                System.out.println(b);
                buf.append(String.format("%02X", b & 0xff));
                System.out.println(String.format("%02X", b & 0xff));
            }
            return buf.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
