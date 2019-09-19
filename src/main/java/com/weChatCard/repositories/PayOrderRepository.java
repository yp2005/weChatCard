package com.weChatCard.repositories;

import com.weChatCard.entities.PayOrder;
import com.weChatCard.entities.PayRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayOrderRepository extends JpaRepository<PayOrder, Integer>, JpaSpecificationExecutor<PayOrder> {
    List<PayOrder> findByPayUserIdAndRechargeStatus(@Param("payUserId") Integer payUserId, @Param("rechargeStatus") Integer rechargeStatus);
}

