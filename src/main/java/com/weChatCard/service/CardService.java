package com.weChatCard.service;

import com.weChatCard.entities.Card;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.vo.CardVo;
import com.weChatCard.vo.ListInput;
import com.weChatCard.vo.ListOutput;
import com.weChatCard.vo.WechatPushData;

import java.util.List;

/**
 * 卡实例服务接口定义
 *
 * @Author: yupeng
 */
public interface CardService {
    /**
     * 添加卡实例信息
     * @param card
     * @return card
     */
    Card add(Card card) throws BusinessException;

    /**
     * 更新卡实例信息
     * @param card
     * @return card
     */
    Card update(Card card) throws BusinessException;

    /**
     * 删除卡实例
     * @param ids
     */
    void delete(List<Integer> ids) throws BusinessException;

    /**
     * 获取卡实例信息
     * @param id
     * @return card
     */
    CardVo get(Integer id) throws BusinessException;

    /**
     * 获取用户的卡实例信息
     * @param userId
     * @return card
     */
    CardVo getByUserId(Integer userId) throws BusinessException;

    /**
     * 根据条件查询卡实例信息列表
     * @param listInput
     * @return ListOutput
     */
    ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 用户提交用户信息后激活会员卡
     * @param wechatPushData
     */
    void activeCard(WechatPushData wechatPushData) throws BusinessException;

}
