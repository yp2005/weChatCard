package com.weChatCard.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 退款请求参数对象
 *
 * @Author: yupeng
 */

public class RefundVo {

    @ApiModelProperty(value = "账单退款ID")
    private Integer billRefundId;

    @ApiModelProperty(value = "支付记录ID")
    private Integer payRecordId;

    @ApiModelProperty(value = "退款金额，必填")
    private String totalAmount;

    @ApiModelProperty(value = "退款原因")
    private String refundReason;

    public void setPayRecordId(Integer payRecordId) {
        this.payRecordId = payRecordId;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public Integer getPayRecordId() {
        return payRecordId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setBillRefundId(Integer billRefundId) {
        this.billRefundId = billRefundId;
    }

    public Integer getBillRefundId() {
        return billRefundId;
    }
}
