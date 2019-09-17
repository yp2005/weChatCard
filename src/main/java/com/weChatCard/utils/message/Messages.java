package com.weChatCard.utils.message;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求服务返回消息枚举
 *
 * @Author: yupeng
 */

public class Messages {
    /**
     * 服务调用成功
     */
    public static Integer CODE_20000 = 20000;
    /**
     * 服务调用成功,但没有找到相应数据
     */
    public static Integer CODE_20001 = 20001;
    /**
     * 错误的请求
     */
    public static Integer CODE_40000 = 40000;
    /**
     * 请求参数不正确
     */
    public static Integer CODE_40001 = 40001;
    /**
     * 请求的服务不存在
     */
    public static Integer CODE_40004 = 40004;
    /**
     * {?}请求参数是必须项目，不能为空
     */
    public static Integer CODE_40005 = 40005;
    /**
     * {?}请求参数类型错误
     */
    public static Integer CODE_40006 = 40006;
    /**
     * 未定义的请求参数{?}
     */
    public static Integer CODE_40007 = 40007;
    /**
     * {?}请求参数数值格式错误
     */
    public static Integer CODE_40008 = 40008;
    /**
     * {?}请求参数数值超出业务范围
     */
    public static Integer CODE_40009 = 40009;
    /**
     * 无法处理请求,原因:{?}
     */
    public static Integer CODE_40010 = 40010;
    /**
     * 用户密码不正确
     */
    public static Integer CODE_40101 = 40101;
    /**
     * 服务器系统异常
     */
    public static Integer CODE_50000 = 50000;
    /**
     * 传入的JSON数据格式错误
     */
    public static Integer CODE_50001 = 50001;
    /**
     * 服务调用失败,原因:{?}
     */
    public static Integer CODE_50002 = 50002;
    /**
     * 通用未知错误
     */
    public static Integer CODE_50003 = 50003;
    /**
     * {?}数据格式错误
     */
    public static Integer CODE_50101 = 50101;
    /**
     * 权限不足，无法执行此操作
     */
    public static Integer CODE_50200 = 50200;
    /**
     * 权限不足，无法查看该信息
     */
    public static Integer CODE_50201 = 50201;
    /**
     * 用户没有登陆或Token已过期
     */
    public static Integer CODE_50401 = 50401;

    /**
     * 消息状态码对应的提示信息
     */
    public final static Map<Integer,String> CODE_MESSAGE_MAP = new HashMap<Integer,String>() {{
        put(CODE_20000, "服务调用成功");
        put(CODE_20001, "服务调用成功,但没有找到相应数据");

        put(CODE_40000, "错误的请求");
        put(CODE_40001, "请求参数不正确");
        put(CODE_40004, "请求的服务不存在");
        put(CODE_40005, "{?}请求参数是必须项目，不能为空");
        put(CODE_40006, "{?}请求参数类型错误");
        put(CODE_40007, "未定义的请求参数{?}");
        put(CODE_40008, "{?}请求参数数值格式错误");
        put(CODE_40009, "{?}数据格式错误");
        put(CODE_40010, "无法处理请求,原因:{?}");

        put(CODE_40101, "用户密码不正确");

        put(CODE_50000, "服务器系统异常");
        put(CODE_50001, "传入的JSON数据格式错误");
        put(CODE_50002, "服务调用失败,原因:{?}");
        put(CODE_50003, "通用未知错误");

        put(CODE_50101, "{?}数据格式错误");
        put(CODE_50200, "权限不足，无法执行此操作");
        put(CODE_50201, "权限不足，无法查看该信息");
        put(CODE_50401, "用户没有登陆或Token已过期");

    }};
}
