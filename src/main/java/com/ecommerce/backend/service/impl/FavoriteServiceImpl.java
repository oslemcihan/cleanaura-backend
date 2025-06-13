package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.model.Favorite;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.FavoriteRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.service.FavoriteService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepo, UserRepository userRepo, ProductRepository productRepo) {
        this.favoriteRepo = favoriteRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }
    @Transactional
    @Override
    public Favorite addFavorite(Long userId, Long productId) {
        User user = userRepo.findById(userId).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();
        if (favoriteRepo.findByUserAndProduct(user, product).isPresent()) {
            throw new IllegalArgumentException("Bu ürün zaten favorilerde.");
        }
        return favoriteRepo.save(Favorite.builder().user(user).product(product).build());
    }
    @Transactional
    @Override
    public void removeFavorite(Long userId, Long productId) {
        User user = userRepo.findById(userId).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();
        favoriteRepo.deleteByUserAndProduct(user, product);
    }

    @Override
    public List<Favorite> getFavoritesByUser(Long userId) {
        return favoriteRepo.findByUserId(userId);
    }
    @Override
    public List<Favorite> getAllFavorites() {
        return favoriteRepo.findAll();
    }

}
