package com.weChatCard.repositories;

import com.weChatCard.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer>,JpaSpecificationExecutor<Card> {
    @Query("select r from Card r,User ur where r.id = ur.cardId and ur.personName = ?1")
    Card queryByPersonName(@Param("personName") String personName);

    @Query("select r from Card r,User ur where r.id = ur.cardId and ur.userId = ?1")
    Card queryByUserId(@Param("userName") Integer userId);
}


