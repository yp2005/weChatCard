package com.weChatCard.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="SearchPara")
public class SearchPara {
    @ApiModelProperty(value="属性名")
    private String name;
    @ApiModelProperty(value = "eq noeq like ge le gt lt isnull isnotnull in")
    private String op;
    @ApiModelProperty(value="值。字符串用\"\",数值型不带引号，日期型格式为yyyy-MM-dd HH:mm:ss字符串")
    private Object value;
    @ApiModelProperty(value = "字段类型，字符串和数值型时可以为空，日期型需要为date")
    private String type;

    public void setName(String name) {
        this.name = name;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getOp() {
        return op;
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
