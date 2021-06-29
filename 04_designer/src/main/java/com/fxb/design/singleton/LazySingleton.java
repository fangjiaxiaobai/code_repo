package com.fxb.design.singleton;

/**
 * 懒汉式 - 单例模式
 * 优点是: 支持延迟加载
 * 缺点是: 初始化对象时需要加锁。并发度为1. 如果此单例会被频繁使用时，就是频繁的加锁，释放锁。导致性能瓶颈。
 *
 * @author fangjiaxiaobai
 * @date 2021-06-16 10:00
 * @since 1.0.0
 */
public class LazySingleton {

    /**
     * 私有化构造方法
     */
    private LazySingleton() {

    }

    /**
     * 内部创建对象
     */
    private static LazySingleton SINGLETON;

    /**
     * 提供给外部使用
     *
     * @return 实例
     */
    public static synchronized LazySingleton getInstance() {
        if (null == SINGLETON) {
            SINGLETON = new LazySingleton();
        }
        return SINGLETON;
    }

}
