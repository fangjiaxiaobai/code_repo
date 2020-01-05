package com.fxb.learn.skiplist01;

import java.util.Random;

/**
 * @author fangjiaxiaobai@gmail.com
 * @date 2020-01-05
 * @since 1.0.0
 */
public class SkipListTest {

    public static void main(String[] args) {
        testInsert();
        testDelete();
        testSearch();
    }

    private static void testSearch() {
        SkipList<Integer, String> skipList = new SkipList<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int n = random.nextInt(100);
            skipList.insert(n, "" + n);
        }
        skipList.show();
        for (int i = 0; i < 100; i++) {
            int key = random.nextInt(100);
            String delete = skipList.search(key);
            System.out.println("要查找的key:" + key + ", 查找的结果:" + delete);
        }
    }

    private static void testDelete() {
        SkipList<Integer, String> skipList = new SkipList<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int n = random.nextInt(10);
            skipList.insert(n, "" + n);
        }

        skipList.show();
        for (int i = 0; i < 100; i++) {
            int key = random.nextInt(10);
            String delete = skipList.delete(key);
            System.out.println("要删除的key:" + key + ", 删除结果:" + delete);
        }
        skipList.show();
    }

    public static void testInsert() {
        SkipList<Integer, String> skipList = new SkipList<>();
        for (int i = 0; i < 100; i++) {
            int n = new Random().nextInt(100);
            skipList.insert(n, "" + n);
        }

        skipList.show();
    }


}
