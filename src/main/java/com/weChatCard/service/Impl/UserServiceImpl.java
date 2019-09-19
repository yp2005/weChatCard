package com.weChatCard.service.Impl;

import com.weChatCard.bo.SearchPara;
import com.weChatCard.bo.SearchParas;
import com.weChatCard.entities.Card;
import com.weChatCard.entities.User;
import com.weChatCard.repositories.CardRepository;
import com.weChatCard.repositories.UserRepository;
import com.weChatCard.service.UserService;
import com.weChatCard.utils.MySpecification;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.message.Messages;
import com.weChatCard.vo.ListInput;
import com.weChatCard.vo.ListOutput;
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

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CardRepository cardRepository) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
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
        Card card = this.cardRepository.findByCardCode(cardCode);
        if (card == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        User user = userRepository.findByCardId(card.getId());
        if (user == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return user;
    }

}
