package com.weChatCard.utils.message;

/**
 * 请求服务返回消息提示语工具类
 *
 * @Author: yupeng
 */

public class MessagesUtil {

    public static String getMessage(Integer code, String... args) {
        //没有状态码，返回通用未知错误
        if(!Messages.CODE_MESSAGE_MAP.containsKey(code)){
            code = Messages.CODE_50003;
        }
        String message = Messages.CODE_MESSAGE_MAP.get(code);
        for(String param : args){
            message = message.replaceFirst("\\{\\?\\}",param);
        }
        return message;
    }

    public static String getMessage(Integer code) {
        //没有状态码，返回通用未知错误
        if(!Messages.CODE_MESSAGE_MAP.containsKey(code)){
            code = Messages.CODE_50003;
        }
        return Messages.CODE_MESSAGE_MAP.get(code);

    }
}
