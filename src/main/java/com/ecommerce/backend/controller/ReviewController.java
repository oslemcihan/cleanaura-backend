package com.ecommerce.backend.controller;

import com.ecommerce.backend.model.Review;
import com.ecommerce.backend.service.ReviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Review> addReview(@PathVariable Long productId,
                                            @RequestBody Review review,
                                            Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(reviewService.addReview(userId, productId, review));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable Long reviewId,
                                               @RequestBody Review updatedReview,
                                               Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(reviewService.updateReview(reviewId, userId, updatedReview));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId, Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.ok("Yorum silindi.");
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
    }
}
