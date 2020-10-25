package com.fxb.algorithm;

/**
 * @author wangxiyue
 * @date 2020-10-25 00:29
 * @since 1.0
 */
public class Demo {

    public static void main(String[] args) {
        test1();
    }

    private static void test1(){
        SnowFlakeGenerator snowFlakeGenerator = new SnowFlakeGenerator(30L, 1L);
        for (int i=0;i<10000;i++) {
            long nextId = snowFlakeGenerator.nextId();
            System.out.println(nextId);
        }
    }

    private static void test2(){
        SnowFlakeGenerator snowFlakeGenerator = new SnowFlakeGenerator(30L, 1L,6,4);
        for (int i=0;i<10;i++) {
            long nextId = snowFlakeGenerator.nextId();
            System.out.println(nextId);
        }

    }
}
