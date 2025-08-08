package com.vaagai.backend.controller;


import com.vaagai.backend.dto.FullProductDTO;
import com.vaagai.backend.dto.ShortProductDTO;
import com.vaagai.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Page<ShortProductDTO> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/{id}")
    public FullProductDTO getProduct(@PathVariable Integer id) throws Exception {
        return productService.getProductById(id);
    }
}
