package com.weChatCard.controller;

import com.weChatCard.config.LoginRequired;
import com.weChatCard.entities.CardType;
import com.weChatCard.entities.User;
import com.weChatCard.service.CardTypeService;
import com.weChatCard.utils.CommonResponse;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.vo.CardTypeVo;
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
 * 卡类型接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/cardType")
@Api(description = "卡类型")
@LoginRequired(adminRequired = "1")
public class CardTypeController {
    @Autowired
    private CardTypeService cardTypeService;

    @PostMapping(path = "/list")
    @ApiOperation(value = "卡类型列表", notes = "查询卡类型信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.cardTypeService.list(listInput));
    }

    @PostMapping
    @ApiOperation(value = "添加卡类型", notes = "添加卡类型信息接口")
    public CommonResponse add(@Validated({CardType.Validation.class}) @RequestBody CardTypeVo cardType, @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.cardTypeService.add(cardType));
    }

    @PutMapping
    @ApiOperation(value = "修改卡类型", notes = "修改卡类型信息接口")
    public CommonResponse update(@Validated({CardType.Validation.class}) @RequestBody CardTypeVo cardType, @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.cardTypeService.update(cardType));
    }

    @DeleteMapping
    @ApiOperation(value = "删除卡类型", notes = "删除卡类型信息接口")
    public CommonResponse delete(@NotNull(message = "卡类型编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        this.cardTypeService.delete(ids);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询卡类型", notes = "根据ID查询卡类型")
    public CommonResponse get(@NotNull(message = "卡类型编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.cardTypeService.get(id));
    }
}
