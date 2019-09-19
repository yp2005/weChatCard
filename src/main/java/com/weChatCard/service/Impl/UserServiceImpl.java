package com.weChatCard.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.weChatCard.bo.SearchPara;
import com.weChatCard.bo.SearchParas;
import com.weChatCard.entities.Card;
import com.weChatCard.entities.User;
import com.weChatCard.redis.RedisClient;
import com.weChatCard.repositories.CardRepository;
import com.weChatCard.repositories.UserRepository;
import com.weChatCard.service.UserService;
import com.weChatCard.utils.Constants;
import com.weChatCard.utils.MySpecification;
import com.weChatCard.utils.WeChatUtil;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.message.Messages;
import com.weChatCard.vo.ListInput;
import com.weChatCard.vo.ListOutput;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务接口实现
 *
 * @Author: yupeng
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    private CardRepository cardRepository;

    private RedisClient redisClient;

    //公众号APPID
    @Value("${SUBSCRIPTION_APP_ID}")
    private String subscriptionAppId;

    //公众号AppSecret
    @Value("${SUBSCRIPTION_APP_SECRET}")
    private String subscriptionAppSecret;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RedisClient redisClient,
                           CardRepository cardRepository) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.redisClient = redisClient;
    }

    @Override
    public User add(User user) throws BusinessException {
        User oldUser = this.userRepository.findByUserName(user.getUserName());
        if(oldUser != null) {
            throw new BusinessException(Messages.CODE_40010, "用户名已存在！");
        }
        user = userRepository.save(user);
        return user;
    }

    @Override
    public User update(User user) throws BusinessException {
        user = userRepository.save(user);
        return user;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        for (int id : ids) {
            userRepository.delete(id);
        }
    }

    @Override
    public ListOutput list(ListInput listInput) throws BusinessException {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        if (StringUtils.isNotBlank(listInput.getSortDirection())
                && StringUtils.isNotBlank(listInput.getSortProperties())) {
            sort = new Sort(Sort.Direction.fromString(listInput.getSortDirection()), listInput.getSortProperties());
        }
        Pageable pageable = null;
        if (listInput.getPage() != null && listInput.getPageSize() != null) {
            pageable = new PageRequest(listInput.getPage(), listInput.getPageSize(), sort);
        }
        ListOutput listOutput = new ListOutput();
        if (pageable != null) {
            Page<User> list = userRepository.findAll(new MySpecification<User>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<User> list = userRepository.findAll(new MySpecification<User>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public ListOutput listByUserType(ListInput listInput, String userType) throws BusinessException {
        SearchPara searchPara = new SearchPara();
        searchPara.setName("userType");
        searchPara.setOp("eq");
        searchPara.setValue(userType);
        if (listInput.getSearchParas() == null) {
            listInput.setSearchParas(new SearchParas());
        }
        if (listInput.getSearchParas().getConditions() == null) {
            listInput.getSearchParas().setConditions(new ArrayList<>());
        }
        listInput.getSearchParas().getConditions().add(searchPara);
        return this.list(listInput);
    }

    @Override
    public User get(Integer id) throws BusinessException {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return user;
    }

    @Override
    public User getByCardCode(String cardCode) throws BusinessException {
        String token;
        WeChatUtil wu = new WeChatUtil();
        try {
            token = this.redisClient.get(Constants.SUBSCRIPTION_ACCESS_TOKEN);
            if (token == null) {
                JSONObject jsonObject = wu.getAccessToken(subscriptionAppId, subscriptionAppSecret);
                token = jsonObject.getString("access_token");
                if (token != null) {
                    redisClient.set(Constants.SUBSCRIPTION_ACCESS_TOKEN, token, 7200);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(Messages.CODE_50000);
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(token)) {
            throw new BusinessException(Messages.CODE_40010);
        }
        JSONObject param = new JSONObject();
        param.put("is_expire_dynamic_code", false);
        param.put("code", cardCode);
        JSONObject result = wu.getCardByCode(token, param);
        if(result.getInteger("errcode").equals(0)) {
            User user = userRepository.findBySubscriptionOpenId(result.getString("openid"));
            if (user == null) {
                throw new BusinessException(Messages.CODE_20001);
            }
            return user;
        }
        else  {
            throw new BusinessException(Messages.CODE_40010, result.toJSONString());
        }
    }

}
