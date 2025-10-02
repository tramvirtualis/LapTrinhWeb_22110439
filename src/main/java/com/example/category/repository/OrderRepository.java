package com.example.category.repository;

import com.example.category.entity.Order;
import com.example.category.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUserOrderByPurchaseDateDesc(User user, Pageable pageable);
    boolean existsByUserAndGame(User user, com.example.category.entity.Game game);
}

