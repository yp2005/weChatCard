package com.weChatCard.service;

import com.weChatCard.entities.Payment;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.vo.CardVo;
import com.weChatCard.vo.ListInput;
import com.weChatCard.vo.ListOutput;

import java.util.List;

/**
 * 支付记录服务接口定义
 *
 * @Author: yupeng
 */
public interface PaymentService {
    /**
     * 添加支付记录信息
     * @param payment
     * @return payment
     */
    Payment add(Payment payment) throws BusinessException;

    /**
     * 更新支付记录信息
     * @param payment
     * @return payment
     */
    Payment update(Payment payment) throws BusinessException;

    /**
     * 删除支付记录
     * @param ids
     */
    void delete(List<Integer> ids) throws BusinessException;

    /**
     * 获取支付记录信息
     * @param id
     * @return payment
     */
    Payment get(Integer id) throws BusinessException;

    /**
     * 根据条件查询支付记录信息列表
     * @param listInput
     * @return ListOutput
     */
    ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据条件查询某个用户的支付记录信息列表
     * @param listInput
     * @param userId
     * @return ListOutput
     */
    ListOutput listByUserId(ListInput listInput, Integer userId) throws BusinessException;

}
