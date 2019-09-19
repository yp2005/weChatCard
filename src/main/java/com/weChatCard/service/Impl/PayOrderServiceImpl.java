package com.weChatCard.service.Impl;

import com.weChatCard.service.PayOrderService;
import com.weChatCard.utils.MySpecification;
import com.weChatCard.utils.message.Messages;
import com.weChatCard.vo.*;
import com.weChatCard.entities.*;
import com.weChatCard.repositories.*;
import com.weChatCard.utils.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


/**
 * 公共订单服务接口实现
 *
 * @Author: yupeng
 */


@Service
@Transactional
public class PayOrderServiceImpl implements PayOrderService{
    private static Logger log = LoggerFactory.getLogger(PayOrderServiceImpl.class);

    private PayOrderRepository payOrderRepository;

    @Autowired
    public PayOrderServiceImpl(PayOrderRepository payOrderRepository){
        this.payOrderRepository = payOrderRepository;
    }

    @Override
    public PayOrder add(PayOrder payOrder) throws BusinessException{
        payOrder = payOrderRepository.save(payOrder);
        return payOrder;
    }

    @Override
    public PayOrder update(PayOrder payOrder) throws BusinessException{
        return this.payOrderRepository.save(payOrder);
    }

    @Override
    public void delete(List<Integer> ids) throws BusinessException{
        for(Integer id:ids){
            payOrderRepository.delete(id);
        }
    }

    @Override
    public ListOutput list(ListInput payOrderListInput) throws BusinessException {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        if(StringUtils.isNotBlank(payOrderListInput.getSortDirection())
                && StringUtils.isNotBlank(payOrderListInput.getSortProperties())){
            sort = new Sort(Sort.Direction.fromString(payOrderListInput.getSortDirection()), payOrderListInput.getSortProperties());
        }
        Pageable pageable = null;
        if(payOrderListInput.getPage() != null && payOrderListInput.getPageSize() != null){
            pageable = new PageRequest(payOrderListInput.getPage(), payOrderListInput.getPageSize(), sort);
        }
        ListOutput payOrderListOutput = new ListOutput();
        if(pageable != null){
            Page list = payOrderRepository.findAll(new MySpecification<PayOrder>(payOrderListInput.getSearchParas()),pageable);
            payOrderListOutput.setPage(payOrderListInput.getPage());
            payOrderListOutput.setPageSize(payOrderListInput.getPageSize());
            payOrderListOutput.setTotalNum((int)list.getTotalElements());
            payOrderListOutput.setList(list.getContent());
        }else {
            List list = payOrderRepository.findAll(new MySpecification<PayOrder>(payOrderListInput.getSearchParas()),sort);
            payOrderListOutput.setTotalNum(list.size());
            payOrderListOutput.setList(list);
        }
        return payOrderListOutput;
    }

    @Override
    public PayOrder get(Integer id) throws BusinessException{
        PayOrder payOrder = payOrderRepository.findOne(id);
        if(payOrder == null){
            throw new BusinessException(Messages.CODE_20001);
        }
        return payOrder;
    }

}
