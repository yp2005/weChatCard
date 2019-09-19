package com.weChatCard.vo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * 微信事件推送数据
 *
 * @Author: yupeng
 */

@JacksonXmlRootElement(localName = "xml")
public class WechatPushData {
    @JacksonXmlProperty(localName = "ToUserName")
    @JacksonXmlCData(value = true)
    private String toUserName;

    @JacksonXmlProperty(localName = "FromUserName")
    @JacksonXmlCData(value = true)
    private String fromUserName;

    @JacksonXmlProperty(localName = "CreateTime")
    private Integer createTime;

    @JacksonXmlProperty(localName = "MsgType")
    @JacksonXmlCData(value = true)
    private String msgType;

    @JacksonXmlProperty(localName = "Event")
    @JacksonXmlCData(value = true)
    private String event;


    @JacksonXmlProperty(localName = "CardId")
    @JacksonXmlCData(value = true)
    private String cardId;

    @JacksonXmlProperty(localName = "UserCardCode")
    @JacksonXmlCData(value = true)
    private String userCardCode;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getUserCardCode() {
        return userCardCode;
    }

    public void setUserCardCode(String userCardCode) {
        this.userCardCode = userCardCode;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
