package com.weChatCard.controller;

import com.alibaba.fastjson.JSONObject;
import com.weChatCard.entities.PayOriginalRecord;
import com.weChatCard.entities.PayRecord;
import com.weChatCard.entities.User;
import com.weChatCard.repositories.PayOrderRepository;
import com.weChatCard.repositories.PayOriginalRecordRepository;
import com.weChatCard.repositories.PayRecordRepository;
import com.weChatCard.service.WeChatPayService;
import com.weChatCard.utils.CommonResponse;
import com.weChatCard.utils.PayUtils;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.message.Messages;
import com.weChatCard.vo.RefundVo;
import com.weChatCard.vo.WeChatPayVo;
import com.weChatCard.wechatpay.sdk.MyWxPayConfig;
import com.weChatCard.wechatpay.sdk.WXPay;
import com.weChatCard.wechatpay.sdk.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信支付接口
 *
 * @Author: yupeng
 */

@RestController
@RequestMapping("/wechatpay")
@Api(description = "微信支付")
public class WeChatPayController {
    private static Logger log = LoggerFactory.getLogger(WeChatPayController.class);

    @Autowired
    private WeChatPayService weChatPayService;
    @Autowired
    private PayRecordRepository payRecordRepository;
    @Autowired
    private PayOriginalRecordRepository payOriginalRecordRepository;
    @Autowired
    private PayOrderRepository payOrderRepository;

    @PostMapping(path = "/webpay")
    @ApiOperation(value = "Web端微信支付接口", notes = "Web端微信支付接口")
    public CommonResponse webPay(@RequestBody WeChatPayVo weChatPayVo, @ApiIgnore User loginUser, HttpServletResponse response) throws BusinessException {
        log.info("WeChatPayController Web Pay !");

        Integer payAction = 1;
        if (CollectionUtils.isNotEmpty(weChatPayVo.getOrderIds())) {
            payAction = 2;
        }
        PayRecord record = PayUtils.createPayRecord(weChatPayVo.getOrderId(), weChatPayVo.getOrderIds(),
                weChatPayVo.getPayType(), payAction, "微信", "WEB",
                loginUser, payRecordRepository, "CNY");
        weChatPayVo.setOutTradeNo(record.getPayOrderNumber());
        weChatPayVo.setPayAction(payAction);

        String webHtml = weChatPayService.webPay(weChatPayVo);
        if (StringUtils.isBlank(webHtml)) {
            throw new BusinessException(Messages.CODE_40000);
        } else {
            CommonResponse commonResponse = null;
            if (webHtml.startsWith("error")) {
                commonResponse = CommonResponse.getInstance(Messages.CODE_80005, webHtml.replace("error-", ""), webHtml.replace("error-", ""));
            } else {
                commonResponse = CommonResponse.getInstance(webHtml);
            }
            return commonResponse;
        }
    }

    @PostMapping(path = "/apppay")
    @ApiOperation(value = "APP端微信支付接口", notes = "APP端微信支付接口")
    public CommonResponse appPay(@RequestBody WeChatPayVo weChatPayVo, @ApiIgnore User loginUser) throws BusinessException {
        log.info("WeChatPayController App Pay !");

        Integer payAction = 1;
        if (CollectionUtils.isNotEmpty(weChatPayVo.getOrderIds())) {
            payAction = 2;
        }
        PayRecord record = PayUtils.createPayRecord(weChatPayVo.getOrderId(), weChatPayVo.getOrderIds(),
                weChatPayVo.getPayType(), payAction, "微信", "APP", loginUser, payRecordRepository, "CNY");
        weChatPayVo.setOutTradeNo(record.getPayOrderNumber());
        weChatPayVo.setPayAction(payAction);

//        Map<String, String> map = weChatPayService.appPay(weChatPayVo);
        Map<String, String> map = new HashMap<>();
        map.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("nonceStr", "test_nonce_str");
        map.put("package", "test_package");
        map.put("signType", "MD5");
        map.put("paySign", "test_pay_sign");
        map.put("result_code", "SUCCESS");
        CommonResponse commonResponse = null;
        String result_code = map.get("result_code");
        if ("SUCCESS".equalsIgnoreCase(result_code)) {
            commonResponse = CommonResponse.getInstance(map);
        } else {
            commonResponse = CommonResponse.getInstance(Messages.CODE_80005, map.get("err_code_des"), map.get("err_code_des"));
        }
        return commonResponse;
    }

    @PostMapping(path = "/getPayResult")
    @ApiOperation(value = "微信支付获取支付结果接口", notes = "微信支付获取支付结果接口")
    public CommonResponse getPayResult(@RequestBody WeChatPayVo weChatPayVo, @ApiIgnore User loginUser) throws BusinessException {
        log.info("WeChatPayController Get Pay Result !");
        CommonResponse commonResponse = CommonResponse.getInstance(weChatPayService.getPayResult(weChatPayVo));
        return commonResponse;
    }

    @PostMapping(path = "/refund")
    @ApiOperation(value = "微信退款接口", notes = "微信退款接口")
    public CommonResponse refund(@RequestBody RefundVo refundVo, @ApiIgnore User loginUser) throws BusinessException {
        log.info("WeChatPayController Refund Pay !");
        CommonResponse commonResponse = CommonResponse.getInstance(weChatPayService.refund(refundVo, loginUser));
        return commonResponse;
    }

    @PostMapping(path = "/callback")
    @ApiOperation(value = "微信支付回调接口", notes = "微信支付回调接口", hidden = true)
    public void payCallback(HttpServletRequest request) throws BusinessException {
        log.info("WeChatPayController Pay Callback !");
        //保存回调原始记录
        PayOriginalRecord payOriginalRecord = new PayOriginalRecord();
        payOriginalRecord.setPayType("微信");
        try {
            // post请求的密文数据
            // sReqData = HttpUtils.PostData();
            ServletInputStream in = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String sReqData = "";
            String itemStr = "";//作为输出字符串的临时串，用于判断是否读取完毕
            while (null != (itemStr = reader.readLine())) {
                sReqData += itemStr;
            }
            log.info(sReqData);

            MyWxPayConfig config = new MyWxPayConfig();
            WXPay wxpay = new WXPay(config);
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(sReqData);  // 转换成map

            payOriginalRecord.setResultInfo(JSONObject.toJSONString(notifyMap));

            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                payOriginalRecord.setVerify("验证签名成功");

                // 签名正确
                // 进行处理。
                // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                //商户订单号
                String out_trade_no = notifyMap.get("out_trade_no");
                //交易号
                String trade_no = notifyMap.get("transaction_id");
                //交易状态
                String trade_status = notifyMap.get("result_code");
                payOriginalRecord.setResultCode(trade_status);

                Integer orderType = PayUtils.getOrderTypeByPayOrderNumber(out_trade_no);
                PayRecord payRecord = payRecordRepository.findFirstByPayOrderNumberAndPayStatusInAndOrderType(out_trade_no, 0, orderType);

                if (payRecord != null) {
                    payRecord.setStatus(2);
                    payRecord.setCallbackParams(sReqData);
                    if ("SUCCESS".equals(trade_status)) {
                        //付款成功
                        if (orderType == 1) {
                            PayUtils.rechargeCallback(payRecord.getOrderNo());
                        } else {
                            PayUtils.payOrderCallback(payRecord, payRecord.getMoney(), payOrderRepository);
                        }
                        payRecord.setPayStatus(1);
                        payRecord.setSerialNumber(trade_no);
                        payRecord.setPlatformReturnStatus(trade_status);
                    } else {
                        payRecord.setPayStatus(2);
                    }
                    payRecordRepository.save(payRecord);
                }
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                log.error("微信支付回调参数签名错误！！！！！");
                payOriginalRecord.setVerify("验证签名失败");
                payOriginalRecord.setResultCode("-99");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //保存回调原始记录
        payOriginalRecordRepository.save(payOriginalRecord);
    }

    public static void main(String[] args) {
        try {
            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            String appId = "wxf509a0889dce9f2a";
            String apiKey = "2398IUEWFlsweI087R2Nvcjiut8742JN";
            String mchId = "1515850271";
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            //加入数据
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            SortedMap<String, String> param = new TreeMap<>();
            param.put("appid", appId);
            param.put("attach", "test");
            param.put("body", "test");
            param.put("detail", "test");
            param.put("mch_id", mchId);
            param.put("nonce_str", WXPayUtil.generateNonceStr());
            param.put("out_trade_no", "20181106002");
            param.put("total_fee", "1");
            param.put("fee_type", "CNY");
            param.put("spbill_create_ip", "8.8.8.8");
//            param.put("time_expire","20181107000000");
            param.put("notify_url", "https://www.xxxxxx.com:9000/pay/pay/wechatpay/callback");
            param.put("trade_type", "APP");//NATIVE

            String info = WXPayUtil.generateSignedXml(param, apiKey);
            log.info(info);
            BufferedOutputStream buffOutStr = new BufferedOutputStream(conn.getOutputStream());
            buffOutStr.write(info.getBytes("UTF-8"));
            buffOutStr.flush();
            buffOutStr.close();

            //获取输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Map<String, String> map = WXPayUtil.xmlToMap(sb.toString());
            String return_msg = map.get("return_msg");
            log.info(return_msg);
            String return_code = map.get("return_code");
            String result_code = map.get("result_code");
            String prepay_id = map.get("prepay_id");
            String code_url = map.get("code_url");
            log.info("return_code:" + return_code);
            log.info("result_code:" + result_code);
            log.info(code_url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
