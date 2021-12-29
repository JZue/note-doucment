package com.jzue.study.leetcode;

import java.util.*;

/**
 * @Author: junzexue
 * @Date: 2019/3/20 下午4:30
 * @Description:
 **/
public class S_1_TwoSum {
    public int[] twoSum(int[] nums, int target) {
        if (nums.length < 1) {
            throw new IllegalArgumentException();
        }
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {
            if (!map.containsKey(nums[i])) {
                map.put(target - nums[i], i);
            } else {
                return new int[]{i, map.get(nums[i])};
            }
        }
        throw new IllegalArgumentException();
    }
}
