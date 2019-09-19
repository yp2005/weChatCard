package com.weChatCard.service.Impl;

import com.weChatCard.service.PayRecordService;
import com.weChatCard.utils.MySpecification;
import com.weChatCard.vo.*;
import com.weChatCard.entities.*;
import com.weChatCard.repositories.*;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.message.Messages;
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
 * 支付记录服务接口实现
 *
 * @Author: yupeng
 */
@Service
@Transactional
public class PayRecordServiceImpl implements PayRecordService {
    private static Logger log = LoggerFactory.getLogger(PayRecordServiceImpl.class);

    private PayRecordRepository payRecordRepository;

    @Autowired
    public PayRecordServiceImpl(PayRecordRepository payRecordRepository) {
        this.payRecordRepository = payRecordRepository;
    }

    @Override
    public PayRecord add(PayRecord payRecord) throws BusinessException {
        String functionName = "添加支付记录";
        if (payRecord.getId() != null) {
            functionName = "编辑支付记录";
        }
        payRecord = payRecordRepository.save(payRecord);
        return payRecord;
    }

    @Override
    public PayRecord update(PayRecord payRecord) throws BusinessException {
        return payRecordRepository.save(payRecord);
    }

    @Override
    public void delete(List<Integer> ids) throws BusinessException {
        for (Integer id : ids) {
            payRecordRepository.delete(id);
        }
    }

    @Override
    public ListOutput list(ListInput payRecordListInput) throws BusinessException {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        if (StringUtils.isNotBlank(payRecordListInput.getSortDirection())
                && StringUtils.isNotBlank(payRecordListInput.getSortProperties())) {
            sort = new Sort(Sort.Direction.fromString(payRecordListInput.getSortDirection()), payRecordListInput.getSortProperties());
        }
        Pageable pageable = null;
        if (payRecordListInput.getPage() != null && payRecordListInput.getPageSize() != null) {
            pageable = new PageRequest(payRecordListInput.getPage(), payRecordListInput.getPageSize(), sort);
        }
        ListOutput payRecordListOutput = new ListOutput();
        if (pageable != null) {
            Page list = payRecordRepository.findAll(new MySpecification<PayRecord>(payRecordListInput.getSearchParas()), pageable);
            payRecordListOutput.setPage(payRecordListInput.getPage());
            payRecordListOutput.setPageSize(payRecordListInput.getPageSize());
            payRecordListOutput.setTotalNum((int) list.getTotalElements());
            payRecordListOutput.setList(list.getContent());
        } else {
            List list = payRecordRepository.findAll(new MySpecification<PayRecord>(payRecordListInput.getSearchParas()), sort);
            payRecordListOutput.setTotalNum(list.size());
            payRecordListOutput.setList(list);
        }
        return payRecordListOutput;
    }

    @Override
    public PayRecord get(Integer id) throws BusinessException {
        PayRecord payRecord = payRecordRepository.findOne(id);
        if (payRecord == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return payRecord;
    }
}
