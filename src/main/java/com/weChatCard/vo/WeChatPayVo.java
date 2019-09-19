package com.weChatCard.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 微信支付请求参数对象
 *
 * @Author: yupeng
 */

public class WeChatPayVo {

    @ApiModelProperty(value = "1-充值余额 2-订单支付")
    private Integer payType;

    @ApiModelProperty(value = "1-单个支付 2-批量支付")
    private Integer payAction;

    @ApiModelProperty(value = "订单ID")
    private Integer orderId;

    @ApiModelProperty(value = "批量支付订单ID集合")
    private List<Integer> orderIds;

    @ApiModelProperty(value = "商户订单号，商户网站订单系统中唯一订单号，必填",hidden = true)
    private String outTradeNo;

    @ApiModelProperty(value = "付款金额，必填")
    private String totalAmount;

    @ApiModelProperty(value = "订单名称，必填",hidden = true)
    private String subject;

    @ApiModelProperty(value = "商品描述，可空",hidden = true)
    private String body;

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setOrderIds(List<Integer> orderIds) {
        this.orderIds = orderIds;
    }

    public List<Integer> getOrderIds() {
        return orderIds;
    }

    public void setPayAction(Integer payAction) {
        this.payAction = payAction;
    }

    public Integer getPayAction() {
        return payAction;
    }
}
