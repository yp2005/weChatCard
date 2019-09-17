package com.weChatCard.utils;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;

/**
 * 用户密码加密
 *
 * @Author: yupeng
 */

public class EncodeUtils {

    public static String encodeSHA(byte[] data) throws Exception {
        // 初始化MessageDigest,SHA即SHA-1的简称
        MessageDigest md = MessageDigest.getInstance("SHA");
        // 执行摘要方法
        byte[] digest = md.digest(data);
        return new HexBinaryAdapter().marshal(digest).toLowerCase();
    }
}
