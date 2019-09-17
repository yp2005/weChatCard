package com.weChatCard.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * 卡类型
 *
 * @Author: yupeng
 */
@Entity
@Table(name = "t_card_type")
@ApiModel(value = "卡类型表")
public class CardType {
    @Id
    @Column(length = 10,nullable = false)
    @ApiModelProperty(value = "ID",hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 50)
    @ApiModelProperty(value = "卡标识-对应微信API返回的card_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cardKey;

    @Column(length = 50)
    @ApiModelProperty(value = "卡名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cardName;

    @Column(length = 10)
    @ApiModelProperty(value = "卡类型，1：黄金会员卡，2：铂金会有卡，3：砖石会员卡 (可能？)")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer cardType;

    @Column(length = 200)
    @ApiModelProperty(value = "描述")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

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

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
