package com.weChatCard.service.Impl;

import com.weChatCard.entities.PayOrder;
import com.weChatCard.entities.PayRecord;
import com.weChatCard.entities.User;
import com.weChatCard.repositories.PayOrderRepository;
import com.weChatCard.repositories.PayRecordRepository;
import com.weChatCard.service.WeChatPayService;
import com.weChatCard.utils.PayUtils;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.message.Messages;
import com.weChatCard.vo.PayParamVo;
import com.weChatCard.vo.RefundVo;
import com.weChatCard.vo.WeChatPayVo;
import com.weChatCard.wechatpay.sdk.MyWxPayConfig;
import com.weChatCard.wechatpay.sdk.WXPayUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 微信支付服务接口实现类
 *
 * @Author: yupeng
 */


@Service
@Transactional
public class WeChatPayServiceImpl implements WeChatPayService {
    private static Logger log = LoggerFactory.getLogger(WeChatPayServiceImpl.class);

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

    private String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    private String refund_url = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    private String notify_url = "https://www.xxxxxx.com:9000/pay/pay/wechatpay/callback";

    @Autowired
    private PayRecordRepository payRecordRepository;
    @Autowired
    private PayOrderRepository payOrderRepository;
    @Autowired
    private EntityManager entityManager;

    @Override
    public String webPay(WeChatPayVo weChatPayVo) throws BusinessException {
        String code_url = "";
        Integer payType = weChatPayVo.getPayType();
        List<Integer> ids = new ArrayList<>();
        if(2 == weChatPayVo.getPayAction()){
            ids = weChatPayVo.getOrderIds();
        }else{
            Integer orderId = weChatPayVo.getOrderId();
            ids.add(orderId);
        }
        //查询订单
        PayParamVo payParamVo = PayUtils.queryOrder(payType,ids);
        try{
            MyWxPayConfig amberWxPayConfig = new MyWxPayConfig();
            String appId = amberWxPayConfig.getAppID();
            String apiKey = amberWxPayConfig.getKey();
            String mchId = amberWxPayConfig.getMchID();
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            //加入数据
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //支付金额单位为分
            int money = (int)(Double.parseDouble(payParamVo.getTotalAmount())*100);

            SortedMap<String,String> param = new TreeMap<>();
            param.put("appid",appId);
            param.put("attach",payParamVo.getSubject());
            param.put("body",payParamVo.getBody());
            param.put("detail",payParamVo.getBody());
            param.put("mch_id",mchId);
            param.put("nonce_str", WXPayUtil.generateNonceStr());
            param.put("out_trade_no",weChatPayVo.getOutTradeNo());
            param.put("total_fee",String.valueOf(money));
            param.put("fee_type","CNY");
            param.put("spbill_create_ip","0.0.0.0");
//            param.put("time_expire","20181107000000");
            param.put("notify_url",notify_url);
            param.put("trade_type","NATIVE");

            String info = WXPayUtil.generateSignedXml(param,apiKey);
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
            log.info("wechat go pay return result : " + return_msg);
            String result_code = map.get("result_code");
            if("SUCCESS".equalsIgnoreCase(result_code)){
                code_url = map.get("code_url");
            }else{
                code_url = "error-"+map.get("err_code_des");
            }
            //支付日志记录
            PayUtils.updatePayRecordRequestCompleted(weChatPayVo.getOutTradeNo(),payType,payParamVo.getOrderNumber(),
                    payParamVo.getTotalAmount(),result_code+":"+code_url,info,payRecordRepository);

            //订单状态
            for(int id: ids){
                PayOrder payOrder = payOrderRepository.findOne(id);
                payOrder.setOrderStatus(2);
                payOrderRepository.save(payOrder);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return code_url;
    }

    @Override
    public Map<String, String> appPay(WeChatPayVo weChatPayVo) throws BusinessException {
        Map<String, String> resultMap = new HashMap<>();
        Integer payType = weChatPayVo.getPayType();
        List<Integer> ids = new ArrayList<>();
        if(2 == weChatPayVo.getPayAction()){
            ids = weChatPayVo.getOrderIds();
        }else{
            Integer orderId = weChatPayVo.getOrderId();
            ids.add(orderId);
        }
        //查询订单
        PayParamVo payParamVo = PayUtils.queryOrder(payType,ids);
        try{
            MyWxPayConfig amberWxPayConfig = new MyWxPayConfig();
            String appId = amberWxPayConfig.getAppID();
            String apiKey = amberWxPayConfig.getKey();
            String mchId = amberWxPayConfig.getMchID();
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            //加入数据
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //支付金额单位为分
            int money = (int)(Double.parseDouble(payParamVo.getTotalAmount())*100);

            SortedMap<String,String> param = new TreeMap<>();
            param.put("appid",appId);
            param.put("attach",payParamVo.getSubject());
            param.put("body",payParamVo.getBody());
            param.put("detail",payParamVo.getBody());
            param.put("mch_id",mchId);
            param.put("nonce_str",WXPayUtil.generateNonceStr());
            param.put("out_trade_no",weChatPayVo.getOutTradeNo());
            param.put("total_fee",String.valueOf(money));
            param.put("fee_type","CNY");
            param.put("spbill_create_ip","0.0.0.0");
//            param.put("time_expire","20181107000000");
            param.put("notify_url",notify_url);
            param.put("trade_type","APP");//NATIVE

            String info = WXPayUtil.generateSignedXml(param,apiKey);
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
            resultMap = WXPayUtil.xmlToMap(sb.toString());
            String return_msg = resultMap.get("return_msg");
            log.info("wechat go pay return result : " + return_msg);
            String result_code = resultMap.get("result_code");
            String prepay_id = "";
            if("SUCCESS".equalsIgnoreCase(result_code)){
                prepay_id = resultMap.get("prepay_id");
            }else{
                prepay_id = resultMap.get("err_code_des");
            }
            //支付日志记录
            PayUtils.updatePayRecordRequestCompleted(weChatPayVo.getOutTradeNo(),payType,payParamVo.getOrderNumber(),
                    payParamVo.getTotalAmount(),result_code+":"+prepay_id,info,payRecordRepository);

            //订单状态
            for(int id: ids){
                PayOrder payOrder = payOrderRepository.findOne(id);
                payOrder.setOrderStatus(2);
                payOrderRepository.save(payOrder);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public boolean getPayResult(WeChatPayVo weChatPayVo) throws BusinessException {
        boolean bool = false;
        List<Integer> ids = new ArrayList<>();
        if(weChatPayVo.getPayAction() != null && 2 == weChatPayVo.getPayAction()){
            ids = weChatPayVo.getOrderIds();
        }else{
            Integer orderId = weChatPayVo.getOrderId();
            ids.add(orderId);
        }
        int countIndex = 0;
        try{
            List<Integer> successList = new ArrayList<>();
            while (countIndex <= 60 && successList.size() < ids.size()){
                for(int id:ids){
                    PayOrder payOrder = (PayOrder)entityManager.createQuery("select o from PayOrder o where o.id = :id").setParameter("id",id).getSingleResult();
                    if(3 == payOrder.getOrderStatus()){
//                        ids.remove(id);
                        if(!successList.contains(id)){
                            successList.add(id);
                        }
                    }
                    entityManager.clear();
                    log.info("loop query order pay status : {} {}  success list : {} ",payOrder.getId(),payOrder.getOrderStatus(),successList.size());
                    if(5 == countIndex){
                        payOrder.setPayTime(simpleDateFormat.format(new Date()));
                        payOrder.setOrderStatus(3);
                        payOrderRepository.save(payOrder);
                    }
                }
                countIndex ++;
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            bool = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return bool;
    }

    @Override
    public String refund(RefundVo refundVo, User loginUser) throws BusinessException {
        try {
            PayRecord payRecord = payRecordRepository.findOne(refundVo.getPayRecordId());
            if(payRecord == null || payRecord.getPayStatus() != 1){
                throw new BusinessException(Messages.CODE_20001);
            }

            //商户订单号，商户网站订单系统中唯一订单号
            String out_trade_no = payRecord.getPayOrderNumber();
            //微信交易号
            String trade_no = payRecord.getSerialNumber();
            //请二选一设置
            //需要退款的金额，该金额不能大于订单金额，必填
            String refund_amount = refundVo.getTotalAmount();
            if(StringUtils.isBlank(refund_amount) && Double.parseDouble(refund_amount) <= 0){
                throw new BusinessException(Messages.CODE_40000);
            }
            //退款的原因说明
            String refund_reason = refundVo.getRefundReason();
            //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
            int num = 1;
            if(StringUtils.isNotBlank(payRecord.getRefundRequestNos())){
                num = payRecord.getRefundRequestNos().split(",").length;
            }
            String out_request_no = payRecord.getPayOrderNumber()+'_'+num;

            MyWxPayConfig amberWxPayConfig = new MyWxPayConfig();
            String appId = amberWxPayConfig.getAppID();
            String apiKey = amberWxPayConfig.getKey();
            String mchId = amberWxPayConfig.getMchID();
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            //加入数据
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            SortedMap<String,String> param = new TreeMap<>();
            param.put("appid",appId);
            param.put("mch_id",mchId);
            param.put("nonce_str",WXPayUtil.generateNonceStr());
            param.put("out_trade_no",out_trade_no);
            param.put("transaction_id",trade_no);
            param.put("out_refund_no",out_request_no);
            param.put("total_fee","1");
            param.put("refund_fee","1");
            param.put("notify_url",notify_url);
            param.put("refund_desc",refund_reason);

            String info = WXPayUtil.generateSignedXml(param,apiKey);
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
            log.info("wechat refund return result : " + return_msg);

            PayUtils.updatePayRecordRefund(refundVo,payRecord,out_request_no,payRecordRepository, loginUser);

            //输出
            return return_msg;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
