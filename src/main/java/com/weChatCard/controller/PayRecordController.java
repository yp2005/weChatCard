package com.weChatCard.controller;

import com.weChatCard.config.LoginRequired;
import com.weChatCard.service.PayRecordService;
import com.weChatCard.vo.ListInput;
import com.weChatCard.entities.PayRecord;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * 支付记录
 *
 * @Author: yupeng
 */

@RestController
@RequestMapping("/payRecord")
@Api(description = "支付记录")
@LoginRequired(adminRequired = "1")
public class PayRecordController {

    private static Logger log = LoggerFactory.getLogger(PayRecordController.class);

    private PayRecordService payRecordService;

    @Autowired
    public PayRecordController(PayRecordService payRecordService){
        this.payRecordService = payRecordService;
    }

    @PostMapping(path = "/list")
    @ApiOperation(value="支付记录列表", notes="查询支付记录信息列表")
    public CommonResponse list(@RequestBody ListInput payRecordListInput)throws BusinessException {
        log.debug("PayRecordController PayRecord List !");
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(payRecordService.list(payRecordListInput));
        return commonResponse;
    }

//    @PostMapping
//    @ApiOperation(value="添加支付记录", notes="添加支付记录信息接口")
//    public CommonResponse add(@RequestBody PayRecord payRecord)throws Exception {
//        log.debug("PayRecordController Add PayRecord !");
//        payRecord = payRecordService.add(payRecord);
//        CommonResponse commonResponse = CommonResponse.getInstance(payRecord);
//        return commonResponse;
//    }
//
//    @PutMapping
//    @ApiOperation(value="修改支付记录", notes="修改支付记录信息接口")
//    public CommonResponse update(@RequestBody PayRecord payRecord)throws BusinessException {
//        log.debug("PayRecordController Update PayRecord !");
//        payRecord = payRecordService.update(payRecord);
//        CommonResponse commonResponse = CommonResponse.getInstance(payRecord);
//        return commonResponse;
//    }
//
//    @DeleteMapping
//    @ApiOperation(value="删除支付记录", notes="删除支付记录信息接口")
//    public CommonResponse delete(@RequestBody List<Integer> ids)throws BusinessException {
//        log.debug("PayRecordController Delete PayRecord !");
//        payRecordService.delete(ids);
//        CommonResponse commonResponse = CommonResponse.getInstance();
//        return commonResponse;
//    }

    @GetMapping(path="/{id}")
    @ApiOperation(value="查询支付记录", notes="根据ID查询支付记录信息")
    public CommonResponse get(@PathVariable Integer id)throws BusinessException {
        log.debug("PayRecordController Get PayRecord !");
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(payRecordService.get(id));
        return commonResponse;
    }
}
