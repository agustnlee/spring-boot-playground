package com.agustin.backend.controller;

import com.agustin.backend.dto.product.CreateProductDto;
import com.agustin.backend.dto.product.ProductCardDto;
import com.agustin.backend.dto.product.ProductDetailDto;
import com.agustin.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
public class ProductController { // WRAPPING IN REQUEST ENTITY for control status code

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // PUBLIC no token
    @GetMapping
    public ResponseEntity<Page<ProductCardDto>> getAll(
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false)    String search,
        @RequestParam(required = false)    String tag
    ) {
        return ResponseEntity.ok(productService.getAll(page, size, search, tag));
    }

    // PUBLIC¿ no token
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    // PROTECTED 
    // consumes multipart bcs accepting image file + json data together
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDetailDto> create(
        @RequestPart("data")  @Valid CreateProductDto dto,
        @RequestPart(value = "image", required = false) MultipartFile image,
        Authentication authentication  // Spring injects on own
    ) {
        Long userId = (Long) authentication.getCredentials(); // from filter
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(productService.create(dto, image, userId));
    }

    // PROTECTED 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete( @PathVariable Long id, Authentication authentication ) {
        Long userId = (Long) authentication.getCredentials();
        productService.delete(id, userId);
        return ResponseEntity.noContent().build();  // 204 
    }
}