package com.weChatCard.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.weChatCard.bo.SearchPara;
import com.weChatCard.bo.SearchParas;
import com.weChatCard.entities.Card;
import com.weChatCard.entities.PayOrder;
import com.weChatCard.entities.Payment;
import com.weChatCard.entities.User;
import com.weChatCard.redis.RedisClient;
import com.weChatCard.repositories.CardRepository;
import com.weChatCard.repositories.PayOrderRepository;
import com.weChatCard.repositories.PaymentRepository;
import com.weChatCard.service.PaymentService;
import com.weChatCard.utils.Constants;
import com.weChatCard.utils.MySpecification;
import com.weChatCard.utils.WeChatUtil;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.message.Messages;
import com.weChatCard.vo.ListInput;
import com.weChatCard.vo.ListOutput;
import org.apache.commons.collections.CollectionUtils;
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
 * 支付记录服务接口实现
 *
 * @Author: yupeng
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private PaymentRepository paymentRepository;
    private RedisClient redisClient;
    private PayOrderRepository payOrderRepository;
    private CardRepository cardRepository;

    //公众号APPID
    @Value("${SUBSCRIPTION_APP_ID}")
    private String subscriptionAppId;

    //公众号AppSecret
    @Value("${SUBSCRIPTION_APP_SECRET}")
    private String subscriptionAppSecret;


    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              PayOrderRepository payOrderRepository,
                              CardRepository cardRepository,
                              RedisClient redisClient) {
        this.paymentRepository = paymentRepository;
        this.redisClient = redisClient;
        this.payOrderRepository = payOrderRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    public Payment add(Payment payment) throws BusinessException {
        payment = this.paymentRepository.save(payment);
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
        Card card = this.cardRepository.findByUserId(payment.getUserId());
        JSONObject param = new JSONObject();
        param.put("card_id", card.getCardKey());
        param.put("code", card.getCardCode());
        // 1-充值 2-消费
        if(payment.getType().equals(1)) {
            card.setMoney(card.getMoney() + payment.getMoney());
            param.put("balance", card.getMoney());
            param.put("add_balance", payment.getMoney());
            param.put("record_balance", "充值");
            param.put("is_notify_balance", true);
            param.put("is_notify_bonus", true);
        }
        else {
            card.setMoney(card.getMoney() - payment.getMoney());
            card.setBonus(card.getBonus() + payment.getMoney().intValue());
            param.put("balance", card.getMoney());
            param.put("add_balance", 0 - payment.getMoney());
            param.put("record_balance", payment.getDescription());
            param.put("bonus", card.getBonus());
            param.put("add_bonus", payment.getMoney().intValue());
            param.put("record_bonus", "消费积分：" + payment.getMoney().intValue());

            param.put("is_notify_balance", true);
            param.put("is_notify_bonus", true);
        }
        JSONObject result = wu.updateMembercard(token, param);
        if(result.getInteger("errcode").equals(0)) {
            this.cardRepository.save(card);
        }
        else {
            throw new BusinessException(Messages.CODE_40010, result.toJSONString());
        }
        return payment;
    }

    @Override
    public Payment update(Payment payment) throws BusinessException {
        return this.paymentRepository.save(payment);
    }

    @Override
    public void delete(List<Integer> ids) throws BusinessException {
        for (Integer id : ids) {
            this.paymentRepository.delete(id);
        }
    }

    @Override
    public Payment get(Integer id) throws BusinessException {
        Payment payment = this.paymentRepository.findOne(id);
        if (payment == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return payment;
    }


    @Override
    public ListOutput list(ListInput listInput) throws BusinessException {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        if (org.apache.commons.lang.StringUtils.isNotBlank(listInput.getSortDirection())
                && org.apache.commons.lang.StringUtils.isNotBlank(listInput.getSortProperties())) {
            sort = new Sort(Sort.Direction.fromString(listInput.getSortDirection()), listInput.getSortProperties());
        }
        Pageable pageable = null;
        if (listInput.getPage() != null && listInput.getPageSize() != null) {
            pageable = new PageRequest(listInput.getPage(), listInput.getPageSize(), sort);
        }
        ListOutput listOutput = new ListOutput();
        if (pageable != null) {
            Page<Payment> list = paymentRepository.findAll(new MySpecification<Payment>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<Payment> list = paymentRepository.findAll(new MySpecification<Payment>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public ListOutput listByUserId(ListInput listInput, Integer userId) throws BusinessException {
        SearchPara searchPara = new SearchPara();
        searchPara.setName("userId");
        searchPara.setOp("eq");
        searchPara.setValue(userId);
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
    public void finishPay(User loginUser) throws BusinessException {
        List<PayOrder> payOrderList = this.payOrderRepository.findByPayUserIdAndRechargeStatus(loginUser.getId(), 0);
        if(CollectionUtils.isNotEmpty(payOrderList)) {
            Double money = 0.0;
            for(PayOrder payOrder : payOrderList) {
                Payment payment = new Payment();
                payment.setUserId(payOrder.getPayUserId());
                payment.setType(1);
                payment.setMoney(payOrder.getMoney());
                money += payOrder.getMoney();
                payment.setDescription("充值");
                payOrder.setRechargeStatus(1);
                this.payOrderRepository.save(payOrder);
                this.paymentRepository.save(payment);
            }
            Card card = this.cardRepository.findByUserId(loginUser.getId());
            card.setMoney(card.getMoney() + money);
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
            JSONObject param = new JSONObject();
            param.put("card_id", card.getCardKey());
            param.put("code", card.getCardCode());
            param.put("balance", card.getMoney());
            param.put("add_balance", money);
            param.put("record_balance", "充值");
            param.put("is_notify_balance", true);
            param.put("is_notify_bonus", true);
            JSONObject result = wu.updateMembercard(token, param);
            if(result.getInteger("errcode").equals(0)) {
                this.cardRepository.save(card);
            }
            else {
                throw new BusinessException(Messages.CODE_40010, result.toJSONString());
            }
        }
    }
}
