package com.weChatCard.utils.exception;

import com.weChatCard.utils.message.Messages;
import com.weChatCard.utils.message.MessagesUtil;

/**
 * 请求服务业务异常
 *
 * @Author: yupeng
 */

public class BusinessException extends Exception {
    private Integer code;

    public BusinessException(Integer code) {
        super(MessagesUtil.getMessage(code));
        this.code = code;
    }

    public BusinessException(Exception e) {
        super(e.getMessage());
        this.code = Messages.CODE_50000;
    }

    public BusinessException(Integer code, String... args) {
        super(MessagesUtil.getMessage(code, args));
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
