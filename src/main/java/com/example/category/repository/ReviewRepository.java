package com.example.category.repository;

import com.example.category.entity.Review;
import com.example.category.entity.User;
import com.example.category.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByGameOrderByReviewDateDesc(Game game, Pageable pageable);
    Optional<Review> findByUserAndGame(User user, Game game);
    boolean existsByUserAndGame(User user, Game game);
}

