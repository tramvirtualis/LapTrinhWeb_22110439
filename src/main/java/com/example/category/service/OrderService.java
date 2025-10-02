package com.example.category.service;

import com.example.category.entity.Game;
import com.example.category.entity.Order;
import com.example.category.entity.User;
import com.example.category.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(User user, Game game) {
        if (orderRepository.existsByUserAndGame(user, game)) {
            throw new IllegalArgumentException("You have already purchased this game");
        }
        Order order = new Order(user, game, game.getPrice());
        return orderRepository.save(order);
    }

    public Page<Order> getUserOrders(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByUserOrderByPurchaseDateDesc(user, pageable);
    }

    public boolean hasUserPurchasedGame(User user, Game game) {
        return orderRepository.existsByUserAndGame(user, game);
    }
}

