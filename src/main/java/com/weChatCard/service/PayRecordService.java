package com.weChatCard.service;

import com.weChatCard.vo.ListInput;
import com.weChatCard.vo.ListOutput;
import com.weChatCard.entities.PayRecord;
import com.weChatCard.utils.exception.BusinessException;

import java.util.List;

/**
 * 支付记录服务接口定义
 *
 * @Author: yupeng
 */

public interface PayRecordService {
    /**
     * 添加支付记录信息
     * @param payRecord
     * @return
     * @throws BusinessException
     */
    public PayRecord add(PayRecord payRecord) throws BusinessException;

    /**
     * 修改支付记录信息
     * @param payRecord
     * @return
     * @throws BusinessException
     */
    public PayRecord update(PayRecord payRecord) throws BusinessException;

    /**
     * 删除支付记录信息
     * @param ids
     * @throws BusinessException
     */
    public void delete(List<Integer> ids) throws BusinessException;

    /**
     * 根据条件查询支付记录信息列表
     * @param listInput
     * @return
     * @throws BusinessException
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取支付记录信息
     * @param id
     * @return
     * @throws BusinessException
     */
    public PayRecord get(Integer id) throws BusinessException;
}
