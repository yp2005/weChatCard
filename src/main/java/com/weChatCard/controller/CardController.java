package com.weChatCard.controller;

import com.weChatCard.config.LoginRequired;
import com.weChatCard.entities.Card;
import com.weChatCard.entities.User;
import com.weChatCard.service.CardService;
import com.weChatCard.utils.CommonResponse;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.vo.CardVo;
import com.weChatCard.vo.ListInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

/**
 * 卡实例接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/card")
@Api(description = "卡实例")
@LoginRequired(adminRequired = "1")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping(path = "/list")
    @ApiOperation(value = "卡实例列表", notes = "查询卡实例信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.cardService.list(listInput));
    }

    @PostMapping
    @ApiOperation(value = "添加卡实例", notes = "添加卡实例信息接口")
    public CommonResponse add(@Validated({Card.Validation.class}) @RequestBody CardVo card, @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.cardService.add(card));
    }

    @PutMapping
    @ApiOperation(value = "修改卡实例", notes = "修改卡实例信息接口")
    public CommonResponse update(@Validated({Card.Validation.class}) @RequestBody CardVo card, @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.cardService.update(card));
    }

    @DeleteMapping
    @ApiOperation(value = "删除卡实例", notes = "删除卡实例信息接口")
    public CommonResponse delete(@NotNull(message = "卡实例编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        this.cardService.delete(ids);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询卡实例", notes = "根据ID查询卡实例")
    public CommonResponse get(@NotNull(message = "卡实例编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.cardService.get(id));
    }

    @GetMapping(path = "/getMy")
    @ApiOperation(value = "查询当前登录用户的Card信息", notes = "查询当前登录用户的Card信息")
    public CommonResponse get(@ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.cardService.getByUserId(loginUser.getId()));
    }
}
