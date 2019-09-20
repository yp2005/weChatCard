package com.weChatCard.service;

import com.weChatCard.entities.Goods;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.vo.ListInput;
import com.weChatCard.vo.ListOutput;

import java.util.List;

/**
 * 商品服务接口定义
 *
 * @Author: yupeng
 */
public interface GoodsService {
    /**
     * 添加商品信息
     * @param goods
     * @return goods
     */
    Goods add(Goods goods) throws BusinessException;

    /**
     * 更新商品信息
     * @param goods
     * @return goods
     */
    Goods update(Goods goods) throws BusinessException;

    /**
     * 删除商品
     * @param ids
     */
    void delete(List<Integer> ids) throws BusinessException;

    /**
     * 获取商品信息
     * @param id
     * @return goods
     */
    Goods get(Integer id) throws BusinessException;

    /**
     * 根据条件查询商品信息列表
     * @param listInput
     * @return ListOutput
     */
    ListOutput list(ListInput listInput) throws BusinessException;
}
