package com.weChatCard.vo;

import com.weChatCard.entities.Card;
import com.weChatCard.entities.CardType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Card VO对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "Card VO对象")
public class CardVo extends Card {
    @ApiModelProperty(value = "卡类型信息")
    private CardType cardType;

    public CardVo(Card card) {
        if (card.getId() != null) {
            this.setId(card.getId());
        }
        this.setCardCode(card.getCardCode());
        this.setCardStatus(card.getCardStatus());
        this.setCardTypeId(card.getCardTypeId());
        this.setCreateTime(card.getCreateTime());
        this.setUpdateTime(card.getUpdateTime());
        this.setMoney(card.getMoney());
        this.setCardKey(card.getCardKey());
    }

    public Card toEntity() {
        Card card = new Card();
        if (this.getId() != null) {
            card.setId(this.getId());
        }
        card.setCardCode(this.getCardCode());
        card.setCardStatus(this.getCardStatus());
        card.setCardTypeId(this.getCardTypeId());
        card.setCreateTime(this.getCreateTime());
        card.setUpdateTime(this.getUpdateTime());
        card.setMoney(this.getMoney());
        card.setCardKey(this.getCardKey());
        return card;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }
}
