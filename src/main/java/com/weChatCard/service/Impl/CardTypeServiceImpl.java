package com.weChatCard.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weChatCard.entities.CardType;
import com.weChatCard.redis.RedisClient;
import com.weChatCard.repositories.CardTypeRepository;
import com.weChatCard.service.CardTypeService;
import com.weChatCard.utils.Constants;
import com.weChatCard.utils.MySpecification;
import com.weChatCard.utils.WeChatUtil;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.message.Messages;
import com.weChatCard.vo.CardTypeVo;
import com.weChatCard.vo.ListInput;
import com.weChatCard.vo.ListOutput;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡类型服务接口实现
 *
 * @Author: yupeng
 */
@Service
public class CardTypeServiceImpl implements CardTypeService {
    private CardTypeRepository cardTypeRepository;

    private RedisClient redisClient;

    @Value("${SUBSCRIPTION_APP_ID}")
    private String subscriptionAppId; //公众号APPID

    @Value("${SUBSCRIPTION_APP_SECRET}")
    private String subscriptionAppSecret; //公众号AppSecret

    @Autowired
    public CardTypeServiceImpl(CardTypeRepository cardTypeRepository, RedisClient redisClient) {
        this.cardTypeRepository = cardTypeRepository;
        this.redisClient = redisClient;
    }

    @Override
    public CardType add(CardTypeVo cardType) throws BusinessException {
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
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(Messages.CODE_40010);
        }
        JSONObject js = wu.createCard(token, JSON.parseObject(cardType.getWechatParam()));
        String card_id = js.getString("card_id");
        if (StringUtils.isEmpty(card_id)) {
            throw new BusinessException(Messages.CODE_40010);
        }
        cardType.setCardKey(card_id);
        return this.cardTypeRepository.save(cardType.toEntity());
    }

    @Override
    public CardType update(CardTypeVo cardType) throws BusinessException {
        WeChatUtil wu = new WeChatUtil();
        String token;
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
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(Messages.CODE_40010);
        }
        JSONObject js = wu.updateCard(token, JSON.parseObject(cardType.getWechatParam()));
        if (js.getInteger("errcode") != 0) {
            throw new BusinessException(Messages.CODE_40010, js.toJSONString());
        }
        return this.cardTypeRepository.save(cardType.toEntity());
    }

    @Override
    public void delete(List<Integer> ids) throws BusinessException {
        String token;
        WeChatUtil wu = new WeChatUtil();
        for (Integer id : ids) {
            CardType cardType = this.get(id);
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
            if (StringUtils.isEmpty(token)) {
                throw new BusinessException(Messages.CODE_40010);
            }
            JSONObject js = wu.deleteCard(token, JSON.parseObject("{\"card_id\": \"" + cardType.getCardKey() + "\"}"));
            Integer errcode = js.getInteger("errcode");
            if (errcode > 0) {
                throw new BusinessException(Messages.CODE_40010);
            }
            this.cardTypeRepository.delete(id);
        }
    }

    @Override
    public CardTypeVo get(Integer id) throws BusinessException {
        CardType cardType = this.cardTypeRepository.findOne(id);
        if (cardType == null) {
            //throw new BusinessException(Messages.CODE_20001);
            return null;
        } else {
            return this.entity2vo(cardType);
        }
    }

    @Override
    public ListOutput list(ListInput listInput) throws BusinessException {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        if (StringUtils.isNotBlank(listInput.getSortDirection()) && StringUtils.isNotBlank(listInput.getSortProperties())) {
            sort = new Sort(Sort.Direction.fromString(listInput.getSortDirection()), listInput.getSortProperties());
        }
        Pageable pageable = null;
        if (listInput.getPage() != null && listInput.getPageSize() != null) {
            pageable = new PageRequest(listInput.getPage(), listInput.getPageSize(), sort);
        }
        ListOutput listOutput = new ListOutput();
        List<CardTypeVo> cardTypeVos = new ArrayList<>();
        List<CardType> lists;
        if (pageable == null) {
            lists = this.cardTypeRepository.findAll(new MySpecification<>(listInput.getSearchParas()));
            listOutput.setTotalNum(lists.size());
        } else {
            Page<CardType> list = this.cardTypeRepository.findAll(new MySpecification<>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            lists = list.getContent();
        }
        lists.forEach(cardType -> cardTypeVos.add(this.entity2vo(cardType)));
        listOutput.setList(cardTypeVos);
        return listOutput;
    }

    private CardTypeVo entity2vo(CardType cardType) {
        CardTypeVo cardTypeVo = new CardTypeVo(cardType);
        return cardTypeVo;
    }
}
