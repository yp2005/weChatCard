package com.weChatCard.utils;

import com.weChatCard.entities.PayOrder;
import com.weChatCard.entities.PayRecord;
import com.weChatCard.entities.User;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.repositories.PayOrderRepository;
import com.weChatCard.repositories.PayRecordRepository;
import com.weChatCard.utils.message.Messages;
import com.weChatCard.vo.PayParamVo;
import com.weChatCard.vo.RefundVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 第三方支付工具类
 *
 * @Author: yupeng
 */

public class PayUtils {

    private static Logger log = LoggerFactory.getLogger(PayUtils.class);

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat monthDateFormat = new SimpleDateFormat("yyyy-MM");

    /**
     * 查询组织支付参数
     *
     * @param payType
     * @param orderIds
     * @return
     * @throws BusinessException
     */
    public static PayParamVo queryOrder(Integer payType, List<Integer> orderIds) throws BusinessException {
        //订单编号
        String orderNumber = System.currentTimeMillis()+"";
        //付款金额，必填
        Double total_amount = 1.0;
        //订单名称，必填
        String subject = "";
        //商品描述，可空
        String body = "";
        String orderNumbersStr = "";
        Integer orderId = null;
        if (orderIds.size() == 1) {
            orderId = orderIds.get(0);
            if (1 == payType) {
                //订单名称，必填
                subject = "充值金额";
                //商品描述，可空
                body = "充值金额";

            } else {
                //订单名称，必填
                subject = "支付";
                //商品描述，可空
                body = "支付";
            }
        } else {
            subject = "批量支付";
            body = "批量支付";
        }

        PayParamVo payParamVo = new PayParamVo();
        payParamVo.setOrderNumber(orderNumber);
        payParamVo.setBody(body);
        payParamVo.setSubject(subject);
        payParamVo.setTotalAmount(String.format("%.2f", total_amount));
        return payParamVo;
    }

    /**
     * 创建支付记录
     *
     * @param orderId
     * @param orderIds
     * @param orderType
     * @param payAction
     * @param payRecordRepository
     * @return
     */
    public static PayRecord createPayRecord(Integer orderId, List<Integer> orderIds,
                                            Integer orderType, Integer payAction,
                                            String payTypeName, String payTerminal, User user,
                                            PayRecordRepository payRecordRepository,String moneyUnit) {
        String linkeOrderIds = "";
        Integer dataId = null;
        if (2 == payAction) {
            //id 排序
            Collections.sort(orderIds, new Comparator<Integer>() {
                @Override
                public int compare(Integer a, Integer b) {
                    return a>b ? 1:-1;
                }
            });
            for (Integer id : orderIds) {
                linkeOrderIds += id + "U";
                if (dataId == null) {
                    dataId = id;
                }
            }
        } else {
            dataId = orderId;
            linkeOrderIds = String.valueOf(orderId);
        }

        String payOrderNumber = createPayOrderNumber(dataId, orderType);
        PayRecord dbPayRecord = payRecordRepository.findFirstByLinkOrderIdsAndPayStatusAndOrderType(linkeOrderIds, 0, orderType);
        if (dbPayRecord != null) {
            dbPayRecord.setPayStatus(-1);
            payRecordRepository.save(dbPayRecord);
            payOrderNumber = dbPayRecord.getPayOrderNumber();
        }

        PayRecord record = new PayRecord();
        record.setOrderId(dataId);
        record.setLinkOrderIds(linkeOrderIds);
        record.setMoneyUnit(moneyUnit);
        record.setPayType(payTypeName);
        record.setPayTerminal(payTerminal);
        record.setPayStatus(0);
        record.setStatus(0);
        record.setPayAction(payAction);
        record.setPayUserName(user.getPersonName());
        record.setPayUserId(user.getId());
        record.setOrderType(orderType);
        record.setPayOrderNumber(payOrderNumber);
        record = payRecordRepository.save(record);

        return record;
    }

    /**
     * 请求第三方支付成功更新支付记录
     *
     * @param payOrderNumber
     * @param payRecordRepository
     * @return
     */
    public static PayRecord updatePayRecordRequestCompleted(String payOrderNumber, Integer payType,
                                                       String orderNumber, String total_amount,
                                                       String signInfo, String paramInfo,
                                                       PayRecordRepository payRecordRepository) throws BusinessException {
        //支付日志记录
        PayRecord record = payRecordRepository.findFirstByPayOrderNumberAndPayStatusInAndOrderType(payOrderNumber, 0, payType);
        if (record == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        if (record.getPayStatus() == -1) {
            throw new BusinessException(Messages.CODE_80005, "重复请求支付");
        }
        record.setOrderNo(orderNumber);
        record.setMoney(Double.valueOf(total_amount));
        record.setSignInfo(signInfo);
        if("WEB".equalsIgnoreCase(record.getPayTerminal())){
            record.setWebPayParamInfo(paramInfo);
        }else{
            record.setAppPayParamInfo(paramInfo);
        }
        record.setStatus(1);
        record = payRecordRepository.save(record);
        return record;
    }

    /**
     * 生成支付记录中传给第三方支付的订单编号
     *
     * @param orderId
     * @return
     */
    public static String createPayOrderNumber(Integer orderId, Integer orderType) {
        String prefix = "";
        if (1 == orderType) {
            prefix = "CZ";
        } else if (2 == orderType) {
            prefix = "PAY";
        }
        String orderNumber = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + orderId;
        return prefix + orderNumber;
    }

    /**
     * 根据支付订单号获取订单类型
     * @param payOrderNumber
     * @return
     */
    public static Integer getOrderTypeByPayOrderNumber(String payOrderNumber){
        Integer orderType;
        if(payOrderNumber.startsWith("CZ")){
            orderType = 1;
        }else if(payOrderNumber.startsWith("ZD")){
            orderType = 3;
        }else if(payOrderNumber.startsWith("ZH")){
            orderType = 4;
        }else{
            orderType = 2;
        }
        return orderType;
    }

    /**
     * 充值回调业务处理
     *
     * @param orderNumber
     * @throws BusinessException
     */
    public static void rechargeCallback(String orderNumber) throws BusinessException {


    }

    /**
     * PO订单支付回调业务处理
     * @param payRecord
     * @param payMoney
     * @throws BusinessException
     */
    public static void payOrderCallback(PayRecord payRecord, double payMoney, PayOrderRepository payOrderRepository) throws BusinessException {
        String[] ss = payRecord.getLinkOrderIds().split("U");
        for(String s:ss){
            if(StringUtils.isBlank(s)){
                continue;
            }
            PayOrder payOrder = payOrderRepository.findOne(Integer.parseInt(s));
            if (payOrder == null) {
                log.error("订单支付回调 " + payOrder.getOrderNumber() + " 找不到对应的订单记录,支付回调更新数据异常!!!");
            } else {
                payOrder.setPayTime(simpleDateFormat.format(new Date()));
                payOrder.setOrderStatus(3);
                payOrder.setPayMoney(payMoney);
                payOrderRepository.save(payOrder);
            }
        }
    }

    /**
     * 退款操作更新支付记录
     * @param refundVo
     * @param payRecord
     * @param payRecordRepository
     */
    public static void updatePayRecordRefund(RefundVo refundVo, PayRecord payRecord, String out_request_no,
                                             PayRecordRepository payRecordRepository, User loginUser){
        double nowRefundMoney = Double.parseDouble(refundVo.getTotalAmount());
        double dbRefundMoney = 0.0;
        if(payRecord.getRefundMoney() != null){
            dbRefundMoney = payRecord.getRefundMoney();
        }
        payRecord.setRefundMoney(dbRefundMoney+nowRefundMoney);
        if(dbRefundMoney+nowRefundMoney >= payRecord.getMoney()){
            payRecord.setPayStatus(4);
        }else{
            payRecord.setPayStatus(3);
        }
        if(StringUtils.isNotBlank(payRecord.getRefundRequestNos())){
            payRecord.setRefundRequestNos(payRecord.getRefundRequestNos()+","+out_request_no);
            payRecord.setRefundReason(payRecord.getRefundReason()+","+refundVo.getRefundReason());
            payRecord.setRefundUserName(payRecord.getRefundUserName()+","+loginUser.getPersonName());
            payRecord.setRefundTime(payRecord.getRefundTime()+","+simpleDateFormat.format(new Date()));
        }else{
            payRecord.setRefundRequestNos(out_request_no);
            payRecord.setRefundReason(refundVo.getRefundReason());
            payRecord.setRefundUserName(loginUser.getPersonName());
            payRecord.setRefundTime(simpleDateFormat.format(new Date()));
        }
        payRecordRepository.save(payRecord);
    }
}
