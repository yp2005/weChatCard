package com.weChatCard.repositories;

import com.weChatCard.entities.PayOriginalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PayOriginalRecordRepository extends JpaRepository<PayOriginalRecord, Integer>, JpaSpecificationExecutor<PayOriginalRecord> {

}

