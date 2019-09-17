package com.weChatCard.vo;

import com.weChatCard.entities.CardType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * CardType VO对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "CardType VO对象")
public class CardTypeVo extends CardType {
    @ApiModelProperty(value = "微信接口参数")
    private String wechatParam;

    public CardTypeVo(CardType cardType) {
        if (cardType.getId() != null) {
            this.setId(cardType.getId());
        }
        this.setCardKey(cardType.getCardKey());
        this.setCardName(cardType.getCardName());
        this.setCreateTime(cardType.getCreateTime());
        this.setUpdateTime(cardType.getUpdateTime());
        this.setDescription(cardType.getDescription());
        this.setCardType(cardType.getCardType());
    }

    public CardType toEntity() {
        CardType cardType = new CardType();
        if (this.getId() != null) {
            cardType.setId(this.getId());
        }
        cardType.setCardKey(this.getCardKey());
        cardType.setCardName(this.getCardName());
        cardType.setCreateTime(this.getCreateTime());
        cardType.setUpdateTime(this.getUpdateTime());
        cardType.setDescription(this.getDescription());
        cardType.setCardType(this.getCardType());
        return cardType;
    }

    public String getWechatParam() {
        return wechatParam;
    }

    public void setWechatParam(String wechatParam) {
        this.wechatParam = wechatParam;
    }
}
