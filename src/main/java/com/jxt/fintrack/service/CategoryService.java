package com.jxt.fintrack.service;

import com.jxt.fintrack.dto.request.CategoryRequest;
import com.jxt.fintrack.dto.response.CategoryResponse;
import com.jxt.fintrack.entity.Category;
import com.jxt.fintrack.entity.User;
import com.jxt.fintrack.repository.CategoryRepository;
import com.jxt.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    public List<CategoryResponse> getAll() {
        User user = getCurrentUser();
        return categoryRepository.findByUser_IdOrIsDefaultTrue(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        User user = getCurrentUser();

        Category category = Category.builder()
                .name(request.getName())
                .icon(request.getIcon())
                .color(request.getColor())
                .isDefault(false)
                .user(user)
                .build();

        return toResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        User user = getCurrentUser();

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));

        if (category.getIsDefault()) {
            throw new RuntimeException("Varsayılan kategoriler düzenlenemez");
        }

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu kategoriye erişim yetkiniz yok");
        }

        category.setName(request.getName());
        category.setIcon(request.getIcon());
        category.setColor(request.getColor());

        return toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void delete(Long id) {
        User user = getCurrentUser();

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));

        if (category.getIsDefault()) {
            throw new RuntimeException("Varsayılan kategoriler silinemez");
        }

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu kategoriye erişim yetkiniz yok");
        }

        categoryRepository.delete(category);
    }

    private CategoryResponse toResponse(Category c) {
        return CategoryResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .icon(c.getIcon())
                .color(c.getColor())
                .isDefault(c.getIsDefault())
                .build();
    }
}