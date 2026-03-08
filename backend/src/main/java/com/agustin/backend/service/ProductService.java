package com.agustin.backend.service;

import com.agustin.backend.dto.product.CreateProductDto;
import com.agustin.backend.dto.product.ProductCardDto;
import com.agustin.backend.dto.product.ProductDetailDto;
import com.agustin.backend.entity.Product;
import com.agustin.backend.entity.Tag;
import com.agustin.backend.entity.User;
import com.agustin.backend.exception.ResourceNotFoundException;
import com.agustin.backend.exception.UnauthorizedException;
import com.agustin.backend.repository.ProductRepository;
import com.agustin.backend.repository.TagRepository;
import com.agustin.backend.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository  productRepository;
    private final UserRepository     userRepository;
    private final TagRepository      tagRepository;
    private final CloudinaryService  cloudinaryService;

    public ProductService(ProductRepository productRepository, UserRepository userRepository, TagRepository tagRepository, 
                        CloudinaryService cloudinaryService) {
        this.productRepository = productRepository;
        this.userRepository    = userRepository;
        this.tagRepository     = tagRepository;
        this.cloudinaryService = cloudinaryService;
    }
    // Handles pagination and slicing

    // GET all with paginated, optional search and tag filter. FOR CARDS ONLY
    @Transactional(readOnly = true)   // keeps session open while mapping DTOs
    public Page<ProductCardDto> getAll(int page, int size, String search, String tag) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("uploadedAt").descending());

        if (search != null && !search.isBlank()) // REPOSITORY automatically limit offset and order
            return productRepository
                .findByTitleContainingIgnoreCase(search, pageable)
                .map(ProductCardDto::new); // for every item create a new dto

        if (tag != null && !tag.isBlank())
            return productRepository
                .findByTags_Name(tag, pageable)
                .map(ProductCardDto::new);

        return productRepository
            .findAll(pageable)
            .map(ProductCardDto::new);
    }

    // GET one — full detail on ONE CARD 
    public ProductDetailDto getById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return new ProductDetailDto(product);
    }

    // CREATE
    @Transactional
    public ProductDetailDto create(CreateProductDto dto, MultipartFile image, Long userId) {
        User owner = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        // upload image to Cloudinary if provided
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = cloudinaryService.upload(image);
        }

        // resolve tag names to Tag entities. If no exist, it creates
        Set<Tag> tags = new HashSet<>();
        for (String tagName : dto.getTags()) {
            Tag tag = tagRepository.findByName(tagName.toLowerCase().trim())
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setName(tagName.toLowerCase().trim());
                    return tagRepository.save(newTag);
                });
            tags.add(tag);
        }

        Product product = new Product();
        product.setTitle(dto.getTitle().trim());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(imageUrl);
        product.setOwner(owner);
        product.setTags(tags);
        product.setAttributes(dto.getAttributes());

        return new ProductDetailDto(productRepository.save(product));
    }

    // DELETE only owner can its own
    @Transactional
    public void delete(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        // check owner
        if (!product.getOwner().getId().equals(userId))
            throw new UnauthorizedException("delete this product");

        // delete image from Cloudinary if exists
        if (product.getImageUrl() != null)
            cloudinaryService.delete(product.getImageUrl());

        productRepository.delete(product);
    }
}