package com.weChatCard.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 列表返回对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "列表返回对象")
public class ListOutput {
    //当前页码
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "第几页")
    private Integer page;
    //每页显示条数
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;
    //总行数
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "总行数")
    private Integer totalNum;
    //列表
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "列表")
    private List list;

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public List getList() {
        return list;
    }
}
