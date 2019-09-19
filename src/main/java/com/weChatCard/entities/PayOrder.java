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
 * 支付公用订单表
 *
 * @Author: yupeng
 */
@Entity
@Table(name = "d_pay_order_1")
@ApiModel(value = "支付公用订单表")
public class PayOrder implements Serializable {
    @Id
    @Column(length = 10,nullable = false)
    @ApiModelProperty(value = "主键ID",hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 255)
    @ApiModelProperty(value = "订单编号")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderNumber;

    @Column(length = 10)
    @ColumnDefault("1")
    @ApiModelProperty(value = "订单状态 0-取消 1-待付款 2-付款中 3-付款成功 4-付款失败")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer orderStatus;

    @Column(length = 255)
    @ApiModelProperty(value = "备注")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String note;

    @Column(length = 255)
    @ApiModelProperty(value = "订单费用")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double money;

    @Column(length = 255)
    @ApiModelProperty(value = "支付人名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String payUserName;

    @Column(length = 11)
    @ApiModelProperty(value = "支付人ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer payUserId;

    @Column(length = 255)
    @ApiModelProperty(value = "支付时间")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String payTime;

    @Column(length = 255)
    @ApiModelProperty(value = "实际支付费用")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double payMoney;

    @Column(length = 255)
    @ApiModelProperty(value = "充值状态 0-未充值 1-已充值")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer rechargeStatus;

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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayUserName() {
        return payUserName;
    }

    public void setPayUserName(String payUserName) {
        this.payUserName = payUserName;
    }

    public Integer getPayUserId() {
        return payUserId;
    }

    public void setPayUserId(Integer payUserId) {
        this.payUserId = payUserId;
    }

    public Double getPayMoney() {
        return payMoney;
    }

    public Integer getRechargeStatus() {
        return rechargeStatus;
    }

    public void setRechargeStatus(Integer rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }

    public void setPayMoney(Double payMoney) {
        this.payMoney = payMoney;
    }
}
