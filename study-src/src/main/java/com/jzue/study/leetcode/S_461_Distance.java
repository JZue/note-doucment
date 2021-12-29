package com.jzue.study.leetcode;

/**
 * @Author: junzexue
 * @Date: 2019/3/20 下午4:54
 * @Description:
 * 两个整数之间的汉明距离指的是这两个数字对应二进制位不同的位置的数目。
 *
 * 给出两个整数 x 和 y，计算它们之间的汉明距离。
 **/
public class S_461_Distance {
    /**
     * @Description:思路  做异或操作，便可知哪些位不同,然后一位一位的判断是不是1(从最低为开始判断，没判断便把最低位丢弃)
     * @Date: 下午4:58 2019/3/20
     **/
    public int hammingDistance(int x, int y) {
        int  distance=0;
        int d=x^y;
        while(d!=0){
            if((d&1)==1) {
                ++distance;
            }
            d>>=1;
        }
        return distance;
    }
}
