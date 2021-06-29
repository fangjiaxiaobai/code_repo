package com.fxb.design.singleton;

/**
 * 饿汉式 - 单例模式
 * 优点是: 将耗时的初始化操作，提前到程序启动的时候完成，这样就能避免在程序运行的时候，再去初始化导致的性能问题。
 *
 * @author fangjiaxiaobai
 * @date 2021-06-16 10:00
 * @since 1.0.0
 */
public class HungrySingleton {

    /**
     * 私有化构造方法
     */
    private HungrySingleton() {

    }

    /**
     * 内部创建对象
     */
    private static final HungrySingleton SINGLETON = new HungrySingleton();

    /**
     * 提供给外部使用
     *
     * @return 实例
     */
    public static HungrySingleton getInstance() {
        return SINGLETON;
    }

}
