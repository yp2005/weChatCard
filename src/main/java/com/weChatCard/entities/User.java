package com.weChatCard.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 *
 * @Author: yupeng
 */
@Entity
@Table(name = "t_user")
@ApiModel(value = "用户表")
public class User implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "用户ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 10)
    @ApiModelProperty(value = "卡ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer cardId;

    @Column(length = 50)
    @ApiModelProperty(value = "姓名或昵称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String personName;

    @Column(length = 50)
    @ApiModelProperty(value = "用户账号")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;

    @Column(length = 50)
    @ApiModelProperty(value = "用户联系电话")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String telphone;

    @Column(length = 50)
    @ApiModelProperty(value = "用户联系邮箱")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @Column(length = 50)
    @ApiModelProperty(value = "用户联系微信")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String wechat;

    @Column(length = 50)
    @ApiModelProperty(value = "小程序用户openId", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String smallProgramOpenId;

    @Column(length = 50)
    @ApiModelProperty(value = "公众号用户openId", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String subscriptionOpenId;

    @Column(length = 50)
    @ApiModelProperty(value = "微信用户唯一标识", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String unionid;

    @Column(length = 50)
    @ApiModelProperty(value = "token 不会入库，登陆返回用户信息时返回" , hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private String token;

    @Column(length = 50)
    @ApiModelProperty(value = "用户类型 1-admin 2-普通用户 ...")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String userType;

    @Lob
    @ApiModelProperty(value = "用户头像")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userImage;

    @Column(length = 255)
    @ApiModelProperty(value = "用户密码")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @Column(updatable = false)
    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createTime;

    @ApiModelProperty(value = "修改时间", hidden = true)
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

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getSmallProgramOpenId() {
        return smallProgramOpenId;
    }

    public void setSmallProgramOpenId(String smallProgramOpenId) {
        this.smallProgramOpenId = smallProgramOpenId;
    }

    public String getSubscriptionOpenId() {
        return subscriptionOpenId;
    }

    public void setSubscriptionOpenId(String subscriptionOpenId) {
        this.subscriptionOpenId = subscriptionOpenId;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
