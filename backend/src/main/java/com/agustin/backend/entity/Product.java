package com.agustin.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


// Entity for E-commerce product, in the case of cards, no need to fetch from all fields (column). Same entity different dto definitions
@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private String imageUrl;  // Cloudinary URL 

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime uploadedAt;  

    // Dynamic attributes for products
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> attributes = new HashMap<>();

    
    @ManyToOne(fetch = FetchType.LAZY) // lazy meaning no need to fetch until ask, standard
    @JoinColumn(name = "user_id", nullable = false) // userid column
    private User owner;

    // Tags  many-many, junction table (for many to many realations)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_tags", 
        joinColumns        = @JoinColumn(name = "product_id"), //FK for product  | product_id | tag_id | FK product id for connect join table
        inverseJoinColumns = @JoinColumn(name = "tag_id") // FK to tags. Sort of mirroring
    )
    private Set<Tag> tags = new HashSet<>();
}
