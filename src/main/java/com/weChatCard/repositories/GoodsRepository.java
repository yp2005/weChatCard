package com.weChatCard.repositories;

import com.weChatCard.entities.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Integer>,JpaSpecificationExecutor<Goods> {
    
}


