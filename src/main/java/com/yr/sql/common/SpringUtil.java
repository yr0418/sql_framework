package com.yr.sql.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * @moduleName: SpringUtil
 * @description: SpringBoot，反射获取 Bean
 * @author: 杨睿
 * @date: 2021-03-20 10:30
 **/

public class SpringUtil {
    private static ApplicationContext applicationContext = null;
    // 非@import显式注入，@Component是必须的，且该类必须与main同包或子包
    // 若非同包或子包，则需手动import 注入，有没有@Component都一样
    // 可复制到Test同包测试

    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 获取applicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);

    }

    /**
     * 通过class获取Bean.
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
