package com.weChatCard.utils;

import org.springframework.context.ApplicationContext;

/**
 * Spring工具类，用于调用getBean方法获取spring管理的对象
 *
 * @Author: yupeng
 */
public class SpringUtil {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }

    public static Object getBean(String beanName) {
        return SpringUtil.applicationContext.getBean(beanName);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz){
        return SpringUtil.applicationContext.getBean(clazz);
    }
}
