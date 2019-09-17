package com.weChatCard.vo;

import com.weChatCard.bo.SearchParas;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 列表传参对象
 *
 * @Author: yupeng
 */
@ApiModel(value = "列表查询入参")
public class ListInput {

    @ApiModelProperty(value = "当前第几页,必选")
    private Integer page;

    @ApiModelProperty(value = "每页显示条数,必选")
    private Integer pageSize;

    @ApiModelProperty(value = "排序方式(asc,desc),可选")
    private String sortDirection;

    @ApiModelProperty(value = "排序属性,可选")
    private String sortProperties;

    @ApiModelProperty(value = "查询条件,可选")
    private SearchParas searchParas;

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public void setSortProperties(String sortProperties) {
        this.sortProperties = sortProperties;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public String getSortProperties() {
        return sortProperties;
    }

    public SearchParas getSearchParas() {
        return searchParas;
    }

    public void setSearchParas(SearchParas searchParas) {
        this.searchParas = searchParas;
    }
}
