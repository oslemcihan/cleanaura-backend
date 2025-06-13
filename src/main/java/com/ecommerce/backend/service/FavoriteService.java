package com.ecommerce.backend.service;

import com.ecommerce.backend.model.Favorite;
import java.util.List;

public interface FavoriteService {
    Favorite addFavorite(Long userId, Long productId);
    void removeFavorite(Long userId, Long productId);
    List<Favorite> getFavoritesByUser(Long userId);
    List<Favorite> getAllFavorites();
}
