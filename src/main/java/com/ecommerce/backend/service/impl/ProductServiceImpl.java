package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.model.Category;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.repository.FavoriteRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FavoriteRepository favoriteRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              FavoriteRepository favoriteRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product newProduct) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ürün bulunamadı: " + id));

        if (newProduct.getName() != null)
            product.setName(newProduct.getName());

        if (newProduct.getDescription() != null)
            product.setDescription(newProduct.getDescription());

        if (newProduct.getPrice() != 0)
            product.setPrice(newProduct.getPrice());

        product.setStock(newProduct.getStock()); // stok 0 olabilir, kontrolsüz güncelle

        if (newProduct.getImageUrl() != null)
            product.setImageUrl(newProduct.getImageUrl());

        if (newProduct.getCategory() != null)
            product.setCategory(newProduct.getCategory());

        return productRepository.save(product);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ürün bulunamadı: " + id));

        // Soft delete: sadece silindi olarak işaretle
        product.setDeleted(true);
        productRepository.save(product);

        // Favori ilişkilerini de temizle (opsiyonel)
        favoriteRepository.deleteByProduct(product);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ürün bulunamadı: " + id));
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findByDeletedFalse();
    }


    @Override
    public List<Product> getByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndDeletedFalse(categoryId);
    }

}
