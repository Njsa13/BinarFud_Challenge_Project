package com.binarfud.binarfud_challenge4.repository;

import com.binarfud.binarfud_challenge4.entity.Merchant;
import com.binarfud.binarfud_challenge4.entity.Order;
import com.binarfud.binarfud_challenge4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query("select o from Order o right join o.user u where o.completed = :completed and u.username = :username")
    List<Order> findByUsernameAndCompleted(String username, Boolean completed);
}
