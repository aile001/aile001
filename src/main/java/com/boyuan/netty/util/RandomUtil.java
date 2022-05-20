package com.boyuan.netty.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;


public class RandomUtil {
    public static void main(String[] args) {
        Random random = new Random();
        int r = random.nextInt(100)+1;//随机生成1-100的数字
        System.out.println(r);
        //随机生成长度为5的数字串

       String s = RandomStringUtils.randomNumeric(18);
       System.out.printf(s);
    }
}
