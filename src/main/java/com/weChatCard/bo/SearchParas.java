package com.weChatCard.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value="SearchParas")
public class SearchParas {
    @ApiModelProperty(value = "多个查询条件")
    private List<SearchPara> conditions;
    @ApiModelProperty(value = "多个条件的关系(AND,OR)")
    private String logic;

    public List<SearchPara> getConditions() {
        return conditions;
    }

    public void setConditions(List<SearchPara> conditions) {
        this.conditions = conditions;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }
}
