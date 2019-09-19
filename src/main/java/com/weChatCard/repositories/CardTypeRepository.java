package com.weChatCard.repositories;

import com.weChatCard.entities.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTypeRepository extends JpaRepository<CardType, Integer>,JpaSpecificationExecutor<CardType> {
    CardType findByCardKey(@Param("cardKey") String cardKey);
}


