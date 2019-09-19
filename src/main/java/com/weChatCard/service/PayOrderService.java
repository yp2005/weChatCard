package com.weChatCard.service;

import com.weChatCard.vo.ListInput;
import com.weChatCard.vo.ListOutput;
import com.weChatCard.entities.PayOrder;
import com.weChatCard.utils.exception.BusinessException;

import java.util.List;

/**
 * 公用订单服务接口定义
 *
 * @Author: yupeng
 */

public interface PayOrderService {
    /**
     * 添加公用订单信息
     *
     * @param payOrder
     * @return
     * @throws BusinessException
     */
    public PayOrder add(PayOrder payOrder) throws BusinessException;

    /**
     * 修改公用订单信息
     *
     * @param payOrder
     * @return
     * @throws BusinessException
     */
    public PayOrder update(PayOrder payOrder) throws BusinessException;

    /**
     * 删除公用订单信息
     *
     * @param ids
     * @throws BusinessException
     */
    public void delete(List<Integer> ids) throws BusinessException;

    /**
     * 根据条件查询公用订单信息列表
     *
     * @param listInput
     * @return
     * @throws BusinessException
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取公用订单信息
     *
     * @param id
     * @return
     * @throws BusinessException
     */
    public PayOrder get(Integer id) throws BusinessException;
}
