package com.weChatCard.controller;

import com.weChatCard.config.LoginRequired;
import com.weChatCard.entities.User;
import com.weChatCard.service.PayOrderService;
import com.weChatCard.vo.ListInput;
import com.weChatCard.entities.PayOrder;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

/**
 * 公用订单接口
 *
 * @Author: yupeng
 */

@RestController
@RequestMapping("/payOrder")
@Api(description = "公用订单")
@LoginRequired
public class PayOrderController {

    private static Logger log = LoggerFactory.getLogger(PayOrderController.class);

    private PayOrderService payOrderService;

    @Autowired
    public PayOrderController(PayOrderService payOrderService){
        this.payOrderService = payOrderService;
    }

    @PostMapping(path = "/list")
    @ApiOperation(value="公用订单列表", notes="查询公用订单信息列表")
    @LoginRequired(adminRequired = "1")
    public CommonResponse list(@RequestBody ListInput payOrderListInput,  @ApiIgnore User loginUser)throws BusinessException {
        log.debug("PayOrderController PayOrder List !");
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(payOrderService.list(payOrderListInput));
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value="添加公用订单", notes="添加公用订单信息接口")
    public CommonResponse add(@RequestBody PayOrder payOrder,  @ApiIgnore User loginUser)throws Exception {
        log.debug("PayOrderController Add PayOrder !");
        payOrder.setOrderStatus(1);
        payOrder.setPayUserId(loginUser.getId());
        payOrder.setPayUserName(loginUser.getPersonName());
        payOrder.setRechargeStatus(0);
        payOrder = payOrderService.add(payOrder);
        CommonResponse commonResponse = CommonResponse.getInstance(payOrder);
        return commonResponse;
    }

    @GetMapping(path="/{id}")
    @ApiOperation(value="查询公用订单", notes="根据ID查询公用订单信息")
    @LoginRequired(adminRequired = "1")
    public CommonResponse get(@PathVariable Integer id,  @ApiIgnore User loginUser)throws BusinessException {
        log.debug("PayOrderController Get PayOrder !");
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(payOrderService.get(id));
        return commonResponse;
    }
}
