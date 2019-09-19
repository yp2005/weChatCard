package com.weChatCard.service;


import com.weChatCard.entities.User;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.vo.ListInput;
import com.weChatCard.vo.ListOutput;

import java.util.List;

/**
 * 用户服务接口定义
 *
 * @Author: yupeng
 */
public interface UserService {
    /**
     * 添加用户信息
     * @param user
     * @return
     */
    public User add(User user) throws BusinessException;

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    public User update(User user) throws BusinessException;

    /**
     * 根据条件查询用户信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据用户类型条件查询用户信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput listByUserType(ListInput listInput, String userType) throws BusinessException;

    /**
     * 根据id获取用户信息
     * @param id
     * @return User
     */
    public User get(Integer id) throws BusinessException;

    /**
     * 根据cardCode获取用户信息
     * @param cardCode
     * @return User
     */
    public User getByCardCode(String cardCode) throws BusinessException;

    /**
     * 修改用户信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
