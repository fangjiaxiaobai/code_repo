package com.fxb.design.singleton;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 枚举实现 - 单例模式
 * 优点是: 通过枚举类型，实现了：实例创建的线程安全性，和实例的唯一性。
 *
 * @author fangjiaxiaobai
 * @date 2021-06-16 10:00
 * @since 1.0.0
 */
public enum EnumSingleton {

    /**
     * 实例对象
     */
    SINGLETON;

    private FileWriter fileWriter;

    {
        try {
            fileWriter = new FileWriter("~/txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 通过调用该方法来完成对象的使用。
     *
     * @return
     */
    public FileWriter get() {
        // do something
        return fileWriter;
    }

}
