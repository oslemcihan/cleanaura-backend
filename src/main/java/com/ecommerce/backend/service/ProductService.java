package com.ecommerce.backend.service;

import com.ecommerce.backend.model.Product;

import java.util.List;

public interface ProductService {
    Product create(Product product);
    Product update(Long id, Product product);
    void delete(Long id);
    Product getById(Long id);
    List<Product> getAll();
    List<Product> getByCategory(Long categoryId);
}
