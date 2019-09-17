package com.weChatCard.service;

import com.weChatCard.entities.CardType;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.vo.CardTypeVo;
import com.weChatCard.vo.ListInput;
import com.weChatCard.vo.ListOutput;

import java.util.List;

/**
 * 卡类型服务接口定义
 *
 * @Author: yupeng
 */
public interface CardTypeService {
    /**
     * 添加卡类型信息
     * @param cardType
     * @return cardType
     */
    CardType add(CardTypeVo cardType) throws BusinessException;

    /**
     * 更新卡类型信息
     * @param cardType
     * @return cardType
     */
    CardType update(CardTypeVo cardType) throws BusinessException;

    /**
     * 删除卡类型
     * @param ids
     */
    void delete(List<Integer> ids) throws BusinessException;

    /**
     * 获取卡类型信息
     * @param id
     * @return cardType
     */
    CardTypeVo get(Integer id) throws BusinessException;

    /**
     * 根据条件查询卡类型信息列表
     * @param listInput
     * @return ListOutput
     */
    ListOutput list(ListInput listInput) throws BusinessException;

}
