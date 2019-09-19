package com.weChatCard.vo;

/**
 * 支付参数对象
 *
 * @Author: yupeng
 */

public class PayParamVo {

    private String body;
    private String subject;
    private String totalAmount;
    private String orderNumber;

    public void setBody(String body) {
        this.body = body;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getBody() {
        return body;
    }

    public String getSubject() {
        return subject;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }
}
