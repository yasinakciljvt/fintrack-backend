package com.jxt.fintrack.repository;

import com.jxt.fintrack.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUser_Id(Long userId);

    List<Category> findByIsDefaultTrue();

    List<Category> findByUser_IdOrIsDefaultTrue(Long userId);
}