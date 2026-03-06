package com.agustin.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

// for setting tags on a product. For now to simplify set fixed tags (in the future could add suggest a tag or smth)

@Data
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;  
}