package com.example.idcardsystem.repository;

import com.example.idcardsystem.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    boolean existsByCode(String code);
    List<Template> findByNameContainingIgnoreCase(String keyword);
}