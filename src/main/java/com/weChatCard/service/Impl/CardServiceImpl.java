package com.weChatCard.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weChatCard.entities.Card;
import com.weChatCard.entities.CardType;
import com.weChatCard.entities.User;
import com.weChatCard.redis.RedisClient;
import com.weChatCard.repositories.CardRepository;
import com.weChatCard.repositories.CardTypeRepository;
import com.weChatCard.service.CardService;
import com.weChatCard.utils.Constants;
import com.weChatCard.utils.MySpecification;
import com.weChatCard.utils.WeChatUtil;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.message.Messages;
import com.weChatCard.vo.CardVo;
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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 卡实例服务接口实现
 *
 * @Author: yupeng
 */
@Service
@Transactional
public class CardServiceImpl implements CardService {
    private CardRepository cardRepository;
    private CardTypeRepository cardTypeRepository;
    private RedisClient redisClient;

    //公众号APPID
    @Value("${SUBSCRIPTION_APP_ID}")
    private String subscriptionAppId;

    //公众号AppSecret
    @Value("${SUBSCRIPTION_APP_SECRET}")
    private String subscriptionAppSecret;


    @Autowired
    public CardServiceImpl(CardRepository cardRepository, CardTypeRepository cardTypeRepository, RedisClient redisClient) {
        this.cardRepository = cardRepository;
        this.cardTypeRepository = cardTypeRepository;
        this.redisClient = redisClient;
    }

    @Override
    public Card add(Card card) throws BusinessException {
        return this.cardRepository.save(card);
    }

    @Override
    public Card update(Card card) throws BusinessException {
        return this.cardRepository.save(card);
    }

    @Override
    public void delete(List<Integer> ids) throws BusinessException {
        String token;
        WeChatUtil wu = new WeChatUtil();
        for (Integer id : ids) {
            Card card = this.get(id);
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
            JSONObject js = wu.unavailableCard(token, JSON.parseObject("{\"code\": \"" + card.getCardCode() + "\", " +
                "\"reason\": \"remove card\"}"));
            Integer errcode = js.getInteger("errcode");
            if (errcode > 0) {
                throw new BusinessException(Messages.CODE_40010);
            }
            this.cardRepository.delete(id);
        }
    }

    @Override
    public CardVo get(Integer id) throws BusinessException {
        Card card = this.cardRepository.findOne(id);
        if (card == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return this.entity2vo(card);
    }

    @Override
    public CardVo getByUserId(Integer userId) throws BusinessException {
        Card card = this.cardRepository.queryByUserId(userId);
        if (card == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return this.entity2vo(card);
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
        List<CardVo> cardVos = new ArrayList<>();
        List<Card> lists;
        if (pageable == null) {
            lists = this.cardRepository.findAll(new MySpecification<>(listInput.getSearchParas()));
            listOutput.setTotalNum(lists.size());
        } else {
            Page<Card> list = this.cardRepository.findAll(new MySpecification<>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            lists = list.getContent();
        }
        lists.forEach(card -> cardVos.add(this.entity2vo(card)));
        listOutput.setList(cardVos);
        return listOutput;
    }

    private CardVo entity2vo(Card card) {
        CardVo cardVo = new CardVo(card);
        if (card.getCardTypeId() != null) {
            CardType cardType = this.cardTypeRepository.findOne(card.getCardTypeId());
            if (cardType != null) {
                cardVo.setCardType(cardType);
            }
        }
        return cardVo;
    }
}
