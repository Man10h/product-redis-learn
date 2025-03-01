package com.web.redis.controller;

import com.web.redis.model.ProductDTO;
import com.web.redis.model.ProductEntity;
import com.web.redis.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    public ResponseEntity<List<ProductEntity>> search(@RequestParam(name = "name") String name) {
        return ResponseEntity.ok(productService.findByName(name));
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.addProduct(productDTO));
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(productDTO));
    }
}
