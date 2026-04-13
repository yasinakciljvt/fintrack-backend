package com.jxt.fintrack.config;

import com.jxt.fintrack.entity.Category;
import com.jxt.fintrack.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (categoryRepository.findByIsDefaultTrue().isEmpty()) {
            List<Category> defaults = List.of(
                    Category.builder().name("Market").icon("🛒").color("#10b981").isDefault(true).build(),
                    Category.builder().name("Fatura").icon("📄").color("#f59e0b").isDefault(true).build(),
                    Category.builder().name("Ulaşım").icon("🚗").color("#3b82f6").isDefault(true).build(),
                    Category.builder().name("Maaş").icon("💰").color("#6366f1").isDefault(true).build(),
                    Category.builder().name("Eğlence").icon("🎮").color("#ec4899").isDefault(true).build(),
                    Category.builder().name("Sağlık").icon("💊").color("#ef4444").isDefault(true).build(),
                    Category.builder().name("Diğer").icon("📦").color("#8b5cf6").isDefault(true).build()
            );
            categoryRepository.saveAll(defaults);
            log.info("Varsayılan kategoriler eklendi");
        }
    }
}