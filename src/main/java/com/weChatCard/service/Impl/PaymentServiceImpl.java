package com.weChatCard.service.Impl;

import com.weChatCard.bo.SearchPara;
import com.weChatCard.bo.SearchParas;
import com.weChatCard.entities.Payment;
import com.weChatCard.redis.RedisClient;
import com.weChatCard.repositories.PaymentRepository;
import com.weChatCard.service.PaymentService;
import com.weChatCard.utils.MySpecification;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.message.Messages;
import com.weChatCard.vo.ListInput;
import com.weChatCard.vo.ListOutput;
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

    //公众号APPID
    @Value("${SUBSCRIPTION_APP_ID}")
    private String subscriptionAppId;

    //公众号AppSecret
    @Value("${SUBSCRIPTION_APP_SECRET}")
    private String subscriptionAppSecret;


    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, RedisClient redisClient) {
        this.paymentRepository = paymentRepository;
        this.redisClient = redisClient;
    }

    @Override
    public Payment add(Payment payment) throws BusinessException {
        return this.paymentRepository.save(payment);
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
}
