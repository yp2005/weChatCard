package com.weChatCard.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * 卡实例
 *
 * @Author: yupeng
 */
@Entity
@Table(name = "t_card")
@ApiModel(value = "卡实例表")
public class Card {
    @Id
    @Column(length = 10,nullable = false)
    @ApiModelProperty(value = "ID",hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 50)
    @ApiModelProperty(value = "对应cardType的cardKey")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cardKey;

    @Column(length = 50)
    @ApiModelProperty(value = "卡号")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cardCode;

    @Column(length = 10)
    @ApiModelProperty(value = "卡状态，0-未领取，1：在用，2：停用，3：过期")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer cardStatus;

    @Column(length = 10)
    @ApiModelProperty(value = "卡类型ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer cardTypeId;

    @Column(length = 20)
    @ApiModelProperty(value = "余额")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double money;

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

    public String getCardKey() {
        return cardKey;
    }

    public void setCardKey(String cardKey) {
        this.cardKey = cardKey;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public Integer getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }

    public Integer getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(Integer cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
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

    public interface Validation{};
}
