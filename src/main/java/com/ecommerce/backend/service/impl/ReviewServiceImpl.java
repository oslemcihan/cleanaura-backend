package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Review;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.ReviewRepository;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.service.ReviewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    public ReviewServiceImpl(ReviewRepository reviewRepo, UserRepository userRepo, ProductRepository productRepo) {
        this.reviewRepo = reviewRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @Override
    public Review addReview(Long userId, Long productId, Review review) {
        User user = userRepo.findById(userId).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();

        review.setUser(user);
        review.setProduct(product);
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepo.save(review);
    }

    @Override
    public Review updateReview(Long reviewId, Long userId, Review updatedReview) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Yorum bulunamadı"));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("Yalnızca kendi yorumunuzu güncelleyebilirsiniz.");
        }

        review.setComment(updatedReview.getComment());
        review.setRating(updatedReview.getRating());

        return reviewRepo.save(review);
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Yorum bulunamadı"));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("Yalnızca kendi yorumunuzu silebilirsiniz.");
        }

        reviewRepo.delete(review);
    }

    @Override
    public List<Review> getReviewsByProduct(Long productId) {
        return reviewRepo.findByProductId(productId);
    }
}
