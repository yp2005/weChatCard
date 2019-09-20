package com.weChatCard.controller;

import com.weChatCard.config.LoginRequired;
import com.weChatCard.entities.Goods;
import com.weChatCard.entities.User;
import com.weChatCard.service.GoodsService;
import com.weChatCard.utils.CommonResponse;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.vo.ListInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 商品接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/goods")
@Api(description = "商品")
@LoginRequired
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping(path = "/list")
    @ApiOperation(value = "商品列表", notes = "查询商品信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.goodsService.list(listInput));
    }

    @PostMapping
    @ApiOperation(value = "添加商品", notes = "添加商品信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse add(@Validated({Goods.Validation.class}) @RequestBody Goods goods, @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.goodsService.add(goods));
    }

    @PutMapping
    @ApiOperation(value = "修改商品", notes = "修改商品信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse update(@Validated({Goods.Validation.class}) @RequestBody Goods goods, @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.goodsService.update(goods));
    }

    @DeleteMapping
    @ApiOperation(value = "删除商品", notes = "删除商品信息接口")
    @LoginRequired(adminRequired = "1")
    public CommonResponse delete(@NotNull(message = "商品编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        this.goodsService.delete(ids);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询商品", notes = "根据ID查询商品")
    public CommonResponse get(@NotNull(message = "商品编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.goodsService.get(id));
    }
}
