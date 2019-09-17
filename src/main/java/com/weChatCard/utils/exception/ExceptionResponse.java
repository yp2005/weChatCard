package com.weChatCard.utils.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.weChatCard.utils.message.Messages;

/**
 * 异常响应返回对象
 *
 * @Author: yupeng
 */

public class ExceptionResponse {

    public static Integer SYSTEM_ERROR_CODE = Messages.CODE_50000;
    public static Integer BINDING_ERROR_CODE = Messages.CODE_40001;

    public static String SYSTEM_ERROR_MESSAGE = Messages.CODE_MESSAGE_MAP.get(Messages.CODE_50000);
    public static String BINDING_ERROR_MESSAGE = Messages.CODE_MESSAGE_MAP.get(Messages.CODE_40001);

    @JsonProperty("code")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer errorCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object errorData;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getErrorData() {
        return errorData;
    }

    public void setErrorData(Object errorData) {
        this.errorData = errorData;
    }
}
