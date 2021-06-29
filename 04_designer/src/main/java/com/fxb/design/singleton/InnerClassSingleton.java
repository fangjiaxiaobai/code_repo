package com.fxb.design.singleton;

/**
 * 静态内部类 - 单例模式
 * 优点是: 线程安全,支持高并发，且可以延迟加载。
 * <p>
 * SingletonHolder 是一个静态内部类，当外部类 IdGenerator 被加载的时候，并不会创建 SingletonHolder 实例对象。
 * 只有当调用 getInstance() 方法时，SingletonHolder 才会被加载，这个时候才会创建 instance。instance 的唯一性、创建过程的线程安全性，
 * 都由 JVM 来保证。所以，这种实现方法既保证了线程安全，又能做到延迟加载
 *
 * @author fangjiaxiaobai
 * @date 2021-06-16 10:00
 * @since 1.0.0
 */
public class InnerClassSingleton {

    /**
     * 私有化构造方法
     */
    private InnerClassSingleton() {

    }

    /**
     * 匿名内部类内部创建单例对象
     */
    private static class SingletonHolder {

        private static final InnerClassSingleton SINGLETON = new InnerClassSingleton();
    }

    /**
     * 提供给外部使用
     *
     * @return 实例
     */
    public static InnerClassSingleton getInstance() {
        return SingletonHolder.SINGLETON;
    }

}
