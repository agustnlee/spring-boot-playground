package com.agustin.backend.dto.product;

import com.agustin.backend.entity.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ProductDetailDto {

    private Long       id;
    private String     title;
    private String     description;
    private BigDecimal price;
    private String     imageUrl;
    private Long       ownerId;
    private String     ownerUsername;
    private Set<String> tags;
    private Map<String, Object> attributes;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime uploadedAt;

    public ProductDetailDto(Product product) {
        this.id            = product.getId();
        this.title         = product.getTitle();
        this.description   = product.getDescription();
        this.price         = product.getPrice();
        this.imageUrl      = product.getImageUrl();
        this.ownerId       = product.getOwner().getId();
        this.ownerUsername = product.getOwner().getUsername();
        this.uploadedAt    = product.getUploadedAt();
        this.attributes    = product.getAttributes();
        this.tags          = product.getTags()
                                .stream()
                                .map(t -> t.getName())
                                .collect(Collectors.toSet());
    }
}
