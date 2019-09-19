package com.weChatCard.controller;

import com.weChatCard.config.LoginRequired;
import com.weChatCard.entities.Payment;
import com.weChatCard.entities.User;
import com.weChatCard.service.PaymentService;
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
 * 会员支付记录接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/payment")
@Api(description = "会员支付记录")
@LoginRequired(adminRequired = "1")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(path = "/list")
    @ApiOperation(value = "支付记录列表", notes = "查询支付记录信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.paymentService.list(listInput));
    }

    @PostMapping(path = "/getMy")
    @ApiOperation(value = "获取我的支付记录列表", notes = "查询我的支付记录信息列表")
    @LoginRequired(adminRequired = "0")
    public CommonResponse getMy(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.paymentService.listByUserId(listInput, loginUser.getId()));
    }

    @PostMapping(path = "/finishPay")
    @ApiOperation(value = "完成充值", notes = "完成充值接口")
    @LoginRequired(adminRequired = "0")
    public CommonResponse finishPay(@ApiIgnore User loginUser) throws BusinessException {
        this.paymentService.finishPay(loginUser);
        return CommonResponse.getInstance();
    }

    @PostMapping
    @ApiOperation(value = "添加支付记录", notes = "添加支付记录信息接口")
    public CommonResponse add(@Validated({Payment.Validation.class}) @RequestBody Payment payment, @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.paymentService.add(payment));
    }

//    @PutMapping
//    @ApiOperation(value = "修改支付记录", notes = "修改支付记录信息接口")
//    public CommonResponse update(@Validated({Payment.Validation.class}) @RequestBody Payment payment, @ApiIgnore User loginUser) throws BusinessException {
//        return CommonResponse.getInstance(this.paymentService.update(payment));
//    }

    @DeleteMapping
    @ApiOperation(value = "删除支付记录", notes = "删除支付记录信息接口")
    public CommonResponse delete(@NotNull(message = "支付记录编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        this.paymentService.delete(ids);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询支付记录", notes = "根据ID查询支付记录")
    public CommonResponse get(@NotNull(message = "支付记录编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        return CommonResponse.getInstance(this.paymentService.get(id));
    }
}
