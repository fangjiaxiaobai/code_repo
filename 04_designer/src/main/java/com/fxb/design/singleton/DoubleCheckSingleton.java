package com.fxb.design.singleton;

/**
 * 双重检测 - 单例模式
 * 优点是: 支持延迟加载,支持高并发。
 * 缺点是:
 *  1、低版本jdk.会因为指令重排，可能会导致单例对象被new出来,赋值给instance之后，还没有来得及初始化，就被其他对象使用。=>通过添加volatile关键字解决。
 *  2、高版本JDK，暂无问题。
 *
 * @author fangjiaxiaobai
 * @date 2021-06-16 10:00
 * @since 1.0.0
 */
public class DoubleCheckSingleton {

    /**
     * 私有化构造方法
     */
    private DoubleCheckSingleton() {

    }

    /**
     * 内部创建对象
     */
    private static DoubleCheckSingleton SINGLETON;

    /**
     * 提供给外部使用
     *
     * @return 实例
     */
    public static synchronized DoubleCheckSingleton getInstance() {
        if (null == SINGLETON) {
            synchronized (DoubleCheckSingleton.class) {
                if(null == SINGLETON) {
                    SINGLETON = new DoubleCheckSingleton();
                }
            }
        }
        return SINGLETON;
    }

}
