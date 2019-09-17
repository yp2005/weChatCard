package com.weChatCard.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.weChatCard.utils.message.Messages;
import com.weChatCard.utils.message.MessagesUtil;

/**
 * 请求服务返回对象
 *
 * @Author: yupeng
 */

public class CommonResponse {

    private CommonResponse() {

    }

    private CommonResponse(Integer errorCode, String errorMessage, Object resultData) {
        this.code = errorCode;
        this.result = resultData;
        this.message = errorMessage;
    }
    public static CommonResponse getInstance(Integer errorCode, Object resultData, String... args) {
        return new CommonResponse(errorCode, MessagesUtil.getMessage(errorCode, args), resultData);
    }

    public static CommonResponse getInstance(Object resultData) {
        return new CommonResponse(Messages.CODE_20000, MessagesUtil.getMessage(Messages.CODE_20000), resultData);
    }

    public static CommonResponse getInstance() {
        return new CommonResponse(Messages.CODE_20000, MessagesUtil.getMessage(Messages.CODE_20000), null);
    }

    @JsonProperty("code")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer code;

    @JsonProperty("message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonProperty("result")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object result;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
