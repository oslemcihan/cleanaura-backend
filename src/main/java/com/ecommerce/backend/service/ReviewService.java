package com.ecommerce.backend.service;

import com.ecommerce.backend.model.Review;

import java.util.List;

public interface ReviewService {
    Review addReview(Long userId, Long productId, Review review);
    Review updateReview(Long reviewId, Long userId, Review updatedReview);
    void deleteReview(Long reviewId, Long userId);
    List<Review> getReviewsByProduct(Long productId);
}
