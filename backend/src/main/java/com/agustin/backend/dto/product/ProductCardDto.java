package com.agustin.backend.dto.product;

import com.agustin.backend.entity.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ProductCardDto {

    private Long       id;
    private String     title;
    private BigDecimal price;
    private String     imageUrl;
    private Long       ownerId;
    private String     ownerUsername;
    private Set<String> tags;

    // maps from entity, for lightweight version for grid of cards
    public ProductCardDto(Product product) {
        this.id            = product.getId();
        this.title         = product.getTitle();
        this.price         = product.getPrice();
        this.imageUrl      = product.getImageUrl();
        this.ownerId       = product.getOwner().getId();
        this.ownerUsername = product.getOwner().getUsername();
        this.tags          = product.getTags()
                                .stream()
                                .map(t -> t.getName())
                                .collect(Collectors.toSet());
    }
}