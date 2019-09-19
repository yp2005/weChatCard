package com.weChatCard.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 第三方支付回调原始记录表
 *
 * @Author: yupeng
 */
@Entity
@Table(name = "d_pay_original_record_1")
@ApiModel(value = "第三方支付回调原始记录表")
public class PayOriginalRecord implements Serializable {
    @Id
    @Column(length = 10,nullable = false)
    @ApiModelProperty(value = "主键ID",hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 255)
    @ApiModelProperty(value = "支付类型")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String payType;

    @Column(length = 255)
    @ApiModelProperty(value = "验证结果")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String verify;

    @Column(length = 255)
    @ApiModelProperty(value = "状态码")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String resultCode;

    @Column(length=10485760)
    @ApiModelProperty(value = "平台交易回调参数信息")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String resultInfo;

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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public String getPayType() {
        return payType;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getVerify() {
        return verify;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultCode() {
        return resultCode;
    }

    public interface AddValidation{};
    public interface UpdateValidation{};
}
