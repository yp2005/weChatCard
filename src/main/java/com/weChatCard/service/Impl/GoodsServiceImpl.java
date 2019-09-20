package com.weChatCard.service.Impl;

import com.weChatCard.entities.Goods;
import com.weChatCard.repositories.GoodsRepository;
import com.weChatCard.service.GoodsService;
import com.weChatCard.utils.MySpecification;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.message.Messages;
import com.weChatCard.vo.ListInput;
import com.weChatCard.vo.ListOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 商品服务接口实现
 *
 * @Author: yupeng
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {
    private GoodsRepository goodsRepository;

    @Autowired
    public GoodsServiceImpl(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;

    }

    @Override
    public Goods add(Goods goods) throws BusinessException {
        return this.goodsRepository.save(goods);
    }

    @Override
    public Goods update(Goods goods) throws BusinessException {
        return this.goodsRepository.save(goods);
    }

    @Override
    public void delete(List<Integer> ids) throws BusinessException {
        for (Integer id : ids) {
            this.goodsRepository.delete(id);
        }
    }

    @Override
    public Goods get(Integer id) throws BusinessException {
        Goods goods = this.goodsRepository.findOne(id);
        if (goods == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return goods;
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
            Page<Goods> list = goodsRepository.findAll(new MySpecification<Goods>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<Goods> list = goodsRepository.findAll(new MySpecification<Goods>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }
}
