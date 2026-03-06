package com.agustin.backend.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

@Data
public class CreateProductDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private String imageUrl; // handled separately

    
    private Set<String> tags = new HashSet<>(); // tag names as strings, services resolve as Obj

    private Map<String, Object> attributes = new HashMap<>(); // dynamic attributes
}