package com.ecommerce.backend.controller;

import com.ecommerce.backend.model.Favorite;
import com.ecommerce.backend.security.CustomUserDetails;
import com.ecommerce.backend.service.FavoriteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@SecurityRequirement(name = "bearerAuth")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Favorite> addFavorite(@PathVariable Long productId, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(favoriteService.addFavorite(userId, productId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeFavorite(@PathVariable Long productId, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.getUser().getId();
        favoriteService.removeFavorite(userId, productId);
        return ResponseEntity.ok("Favorilerden çıkarıldı.");
    }

    @GetMapping
    public ResponseEntity<List<Favorite>> getFavorites(Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(favoriteService.getFavoritesByUser(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<List<Favorite>> getAllFavoritesForAdmin() {
        List<Favorite> favorites = favoriteService.getAllFavorites();
        return ResponseEntity.ok(favorites);
    }
}
