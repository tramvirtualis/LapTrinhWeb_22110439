package com.example.category.service;

import com.example.category.entity.Game;
import com.example.category.entity.Review;
import com.example.category.entity.User;
import com.example.category.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderService orderService;

    public Review createReview(User user, Game game, Integer rating, String comment) {
        // Check if user has purchased the game
        if (!orderService.hasUserPurchasedGame(user, game)) {
            throw new IllegalArgumentException("You can only review games you have purchased");
        }
        
        // Check if user already reviewed this game
        if (reviewRepository.existsByUserAndGame(user, game)) {
            throw new IllegalArgumentException("You have already reviewed this game");
        }

        Review review = new Review(user, game, rating, comment);
        return reviewRepository.save(review);
    }

    public Review updateReview(Long reviewId, User user, Integer rating, String comment) {
        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new IllegalArgumentException("Review not found");
        }
        
        Review review = reviewOpt.get();
        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only edit your own reviews");
        }

        review.setRating(rating);
        review.setComment(comment);
        return reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId, User user) {
        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new IllegalArgumentException("Review not found");
        }
        
        Review review = reviewOpt.get();
        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }

    public Page<Review> getGameReviews(Game game, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewRepository.findByGameOrderByReviewDateDesc(game, pageable);
    }

    public Optional<Review> getUserReviewForGame(User user, Game game) {
        return reviewRepository.findByUserAndGame(user, game);
    }

    public void deleteReviewByAdmin(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}

