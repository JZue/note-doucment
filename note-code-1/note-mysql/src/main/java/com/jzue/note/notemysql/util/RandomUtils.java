package com.jzue.note.notemysql.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: junzexue
 * @Date: 2019/8/19 上午10:47
 * @Description:
 **/
public class RandomUtils {

    /**
     * 随机时间生成器
     **/
    public static Date randomDate(String beginDate, String endDate){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);
            Date end = format.parse(endDate);

            if(start.getTime() >= end.getTime()){
                return null;
            }
            long date = random(start.getTime(),end.getTime());
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long random(long begin,long end){
        long rtn = begin + (long)(Math.random() * (end - begin));
        if(rtn == begin || rtn == end){
            return random(begin,end);
        }
        return rtn;
    }
}
