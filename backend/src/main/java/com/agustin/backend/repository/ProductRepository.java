package com.agustin.backend.repository;

import com.agustin.backend.entity.Product;
import com.agustin.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable); // for pagination without boilerplate i suppose. query interface autom
    Page<Product> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Product> findByTags_Name(String tagName, Pageable pageable);
    Page<Product> findByOwner(User owner, Pageable pageable);
}