package com.weChatCard.repositories;


import com.weChatCard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>,JpaSpecificationExecutor<User> {
    User findByUserName(@Param("userName") String userName);

    User findBySubscriptionOpenId(@Param("subscriptionOpenId") String subscriptionOpenId);

    User findByCardId(@Param("cardId") Integer cardId);
}
