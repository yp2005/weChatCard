package com.weChatCard.service;

/**
 * Web端支付服务接口定义
 *
 * @Author: yupeng
 */
import com.weChatCard.entities.User;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.vo.RefundVo;
import com.weChatCard.vo.WeChatPayVo;

import java.util.Map;

public interface WeChatPayService {
    /**
     * Web端支付
     * @param weChatPayVo
     * @return
     * @throws BusinessException
     */
    public String webPay(WeChatPayVo weChatPayVo) throws BusinessException;

    /**
     * App端支付
     * @param weChatPayVo
     * @return
     * @throws BusinessException
     */
    public Map<String, String> appPay(WeChatPayVo weChatPayVo) throws BusinessException;

    /**
     * 服务轮询查询支付结果返回前端
     * @param weChatPayVo
     * @return
     * @throws BusinessException
     */
    public boolean getPayResult(WeChatPayVo weChatPayVo) throws BusinessException;

    /**
     * 退款接口
     * @param refundVo
     * @throws BusinessException
     */
    public String refund(RefundVo refundVo, User loginUser) throws BusinessException;
}
