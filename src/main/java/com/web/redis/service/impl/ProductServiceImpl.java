package com.web.redis.service.impl;

import com.web.redis.model.ProductDTO;
import com.web.redis.model.ProductEntity;
import com.web.redis.repository.ProductRepository;
import com.web.redis.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@EnableCaching
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;


    // value = "prefix-key/cache-name"
    //Key-default is a arg or which one u config,
    // combine to make key: product::Laptop
    @Cacheable(value = "product", key = "#name")
    @Override
    public List<ProductEntity> findByName(String name) {
        doLongRunningTask();
        return productRepository.findByName(name);
    }

    @Override
    public String addProduct(ProductDTO productDTO) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(productDTO.getName());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setDescription(productDTO.getDescription());
        productEntity.setType(productDTO.getType());
        productRepository.save(productEntity);
        return "add product success";
    }

    // value = "cache-name" where to delete key have prefix product
    // and delete key which have key element productDTO.id
    @CacheEvict(value = "product", key = "#productDTO.id")
    @Override
    public String updateProduct(ProductDTO productDTO) {
        Optional<ProductEntity> optional = productRepository.findById(productDTO.getId());
        if(optional.isEmpty()){
            return "product not found";
        }
        ProductEntity productEntity = optional.get();
        if(productDTO.getName() != null){
            productEntity.setName(productDTO.getName());
        }
        if(productDTO.getPrice() != null){
            productEntity.setPrice(productDTO.getPrice());
        }
        if(productDTO.getDescription() != null){
            productEntity.setDescription(productDTO.getDescription());
        }
        productRepository.save(productEntity);
        return "update product success";
    }


    // function to test cache, if we have info in the cache, we will be received faster
    private void doLongRunningTask() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
