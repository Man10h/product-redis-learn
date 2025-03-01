package com.web.redis.service;

import com.web.redis.model.ProductDTO;
import com.web.redis.model.ProductEntity;

import java.util.List;

public interface ProductService {
    public List<ProductEntity> findByName(String name);

    public String addProduct(ProductDTO productDTO);

    public String updateProduct(ProductDTO productDTO);
}
