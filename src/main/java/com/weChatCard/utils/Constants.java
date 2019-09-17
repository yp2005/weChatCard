package com.weChatCard.utils;

/**
 * 常量
 *
 * @author yupeng
 */
public interface Constants {
    /*****************用户名常量*****************/
    String ADMIN_USER_NAME = "admin"; // 管理员

    /*****************REDIS数据前缀*****************/
    String USER_TOKEN = "user_token#"; // 用户token
    String SUBSCRIPTION_SESSION_KEY_PRE = "subscriptionSessionKeyPre#"; // 公众号会话秘钥
    String SUBSCRIPTION_ACCESS_TOKEN = "subscription#access_token"; // 公众号token的key

    /***************用户类型*****************/
    String USER_TYPE_ADMIN = "1"; // 管理员
    String USER_TYPE_TEACHER = "2"; // 普通用户

    /***************数字类常量*****************/
    Integer TOKEN_EXPIRE_TIME = 30 * 60; // 用户token过期时间30分钟


}
