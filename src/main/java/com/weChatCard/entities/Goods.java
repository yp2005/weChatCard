package com.weChatCard.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * 商品
 *
 * @Author: yupeng
 */
@Entity
@Table(name = "t_goods")
@ApiModel(value = "商品表")
public class Goods {
    @Id
    @Column(length = 10,nullable = false)
    @ApiModelProperty(value = "ID",hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 255)
    @ApiModelProperty(value = "商品名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @Column(length = 500)
    @ApiModelProperty(value = "标题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;

    @Column(length = 1000)
    @ApiModelProperty(value = "简介")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String introduction;

    @Column(length = 255)
    @ApiModelProperty(value = "品牌")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String brand;

    @Column(length = 255)
    @ApiModelProperty(value = "型号")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String model;

    @Column(length = 20)
    @ApiModelProperty(value = "原价")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double price;

    @Column(length = 20)
    @ApiModelProperty(value = "优惠价")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double discountPrice;

    @Column(length = 2000)
    @ApiModelProperty(value = "图片")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pictures;

    @Lob
    @ApiModelProperty(value = "详情")
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public interface Validation{};
}
