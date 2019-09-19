package com.weChatCard.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 支付记录表
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "d_pay_record_1")
@ApiModel(value = "支付记录表")
public class PayRecord implements Serializable {
    @Id
    @Column(length = 10,nullable = false)
    @ApiModelProperty(value = "主键ID",hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 255)
    @ApiModelProperty(value = "1-充值 2-订单支付")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer orderType;

    @Column(length = 255)
    @ApiModelProperty(value = "生成唯一编号第三方支付订单号")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String payOrderNumber;

    @Column(length = 10)
    @ColumnDefault("1")
    @ApiModelProperty(value = "1-单笔支付 2-批量支付")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer payAction;

    @Column(length = 255)
    @ApiModelProperty(value = "支付人")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String payUserName;

    @Column(length = 11)
    @ApiModelProperty(value = "支付人ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer payUserId;

    @Column(length = 255)
    @ApiModelProperty(value = "单个支付，关联订单ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer orderId;

    @Column(length=10485760)
    @ApiModelProperty(value = "关联订单编号")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderNo;

    @Column(length = 255)
    @ApiModelProperty(value = "支付金额")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double money;

    @Column(length = 255)
    @ApiModelProperty(value = "支付金额类型")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String moneyUnit;

    @Column(length = 255)
    @ApiModelProperty(value = "支付类型")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String payType;

    @Column(length = 255)
    @ApiModelProperty(value = "支付终端：web,app")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String payTerminal;

    @Column(length = 255)
    @ColumnDefault("0")
    @ApiModelProperty(value = "0-提交付款 1-调用第三方支付生成订单或得到支付流水号 2-第三方支付回调完成")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer status;

    @Column(length = 255)
    @ColumnDefault("0")
    @ApiModelProperty(value = "-1-已失效 0-未付款 1-付款完成 2-付款失败 3-部分退款 4-全部退款")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer payStatus;

    @Column(length = 255)
    @ColumnDefault("0")
    @ApiModelProperty(value = "平台交易流水号")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String serialNumber;

    //10 MIB似乎是varchar在PostgreSQL中最大值。text差不多
    @Column(length=10485760)
//    @Column(columnDefinition="text")
    @ApiModelProperty(value = "平台交易回调参数信息")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String callbackParams;

    @Column(length = 255)
    @ColumnDefault("0")
    @ApiModelProperty(value = "平台交易返回的原始状态")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String platformReturnStatus;

    @Column(length=10485760)
    @ApiModelProperty(value = "退款请求编号，每次唯一")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refundRequestNos;

    @Column(length = 255)
    @ColumnDefault("0")
    @ApiModelProperty(value = "退款金额")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double refundMoney;

    @Column(length=10485760)
    @ApiModelProperty(value = "退款原因")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refundReason;

    @Column(length=10485760)
    @ApiModelProperty(value = "退款操作人")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refundUserName;

    @Column(length=10485760)
    @ApiModelProperty(value = "退款时间")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refundTime;

    @Column(length=10485760)
    @ApiModelProperty(value = "调用第三方接口生成订单或生成签名核心信息")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String signInfo;

    @Column(length=10485760)
    @ApiModelProperty(value = "APP支付参数信息")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String appPayParamInfo;

    @Column(length=10485760)
    @ApiModelProperty(value = "WEB支付参数信息")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String webPayParamInfo;

    @Column(length=10485760)
    @ApiModelProperty(value = "支付回调内容信息")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String payCallBackInfo;

    @Column(length=10485760)
    @ApiModelProperty(value = "批量支付，关联订单ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String linkOrderIds;

    @Column(updatable = false)
    @ApiModelProperty(value = "创建时间",hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createTime;

    @ApiModelProperty(value = "修改时间",hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public Double getMoney() {
        return money;
    }

    public String getPayType() {
        return payType;
    }

    public String getPayTerminal() {
        return payTerminal;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public String getSignInfo() {
        return signInfo;
    }

    public String getAppPayParamInfo() {
        return appPayParamInfo;
    }

    public String getWebPayParamInfo() {
        return webPayParamInfo;
    }

    public String getPayCallBackInfo() {
        return payCallBackInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public void setPayTerminal(String payTerminal) {
        this.payTerminal = payTerminal;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public void setSignInfo(String signInfo) {
        this.signInfo = signInfo;
    }

    public void setAppPayParamInfo(String appPayParamInfo) {
        this.appPayParamInfo = appPayParamInfo;
    }

    public void setWebPayParamInfo(String webPayParamInfo) {
        this.webPayParamInfo = webPayParamInfo;
    }

    public void setPayCallBackInfo(String payCallBackInfo) {
        this.payCallBackInfo = payCallBackInfo;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setPayUserName(String payUserName) {
        this.payUserName = payUserName;
    }

    public String getPayUserName() {
        return payUserName;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setPlatformReturnStatus(String platformReturnStatus) {
        this.platformReturnStatus = platformReturnStatus;
    }

    public String getPlatformReturnStatus() {
        return platformReturnStatus;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setRefundRequestNos(String refundRequestNos) {
        this.refundRequestNos = refundRequestNos;
    }

    public void setRefundMoney(Double refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getRefundRequestNos() {
        return refundRequestNos;
    }

    public Double getRefundMoney() {
        return refundMoney;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public void setRefundUserName(String refundUserName) {
        this.refundUserName = refundUserName;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public String getRefundUserName() {
        return refundUserName;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setMoneyUnit(String moneyUnit) {
        this.moneyUnit = moneyUnit;
    }

    public String getMoneyUnit() {
        return moneyUnit;
    }

    public void setCallbackParams(String callbackParams) {
        this.callbackParams = callbackParams;
    }

    public String getCallbackParams() {
        return callbackParams;
    }

    public void setLinkOrderIds(String linkOrderIds) {
        this.linkOrderIds = linkOrderIds;
    }

    public String getLinkOrderIds() {
        return linkOrderIds;
    }

    public void setPayOrderNumber(String payOrderNumber) {
        this.payOrderNumber = payOrderNumber;
    }

    public void setPayAction(Integer payAction) {
        this.payAction = payAction;
    }

    public Integer getPayAction() {
        return payAction;
    }

    public String getPayOrderNumber() {
        return payOrderNumber;
    }

    public Integer getPayUserId() {
        return payUserId;
    }

    public void setPayUserId(Integer payUserId) {
        this.payUserId = payUserId;
    }

    public interface AddValidation{};
    public interface UpdateValidation{};
}
