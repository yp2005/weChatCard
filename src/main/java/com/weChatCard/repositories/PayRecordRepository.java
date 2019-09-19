package com.weChatCard.repositories;

import com.weChatCard.entities.PayRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRecordRepository extends JpaRepository<PayRecord, Integer>, JpaSpecificationExecutor<PayRecord> {
    PayRecord findFirstByPayOrderNumberAndPayStatusInAndOrderType(@Param("payOrderNumber")String payOrderNumber,@Param("payStatus")Integer payStatus,@Param("orderType")Integer orderType);
    PayRecord findFirstByLinkOrderIdsAndPayStatusAndOrderType(String linkOrderIds, Integer payStatus, Integer orderType);
}

