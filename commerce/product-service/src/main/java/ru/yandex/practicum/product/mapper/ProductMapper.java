package ru.yandex.practicum.product.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.product.dto.CategoryDto;
import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.ProductDto;
import ru.yandex.practicum.product.entity.Category;
import ru.yandex.practicum.product.entity.Product;

@UtilityClass
public class ProductMapper {

    public static ProductDto mapToDto(Product entity) {
        CategoryDto categoryDto = null;
        if (entity.getCategory() != null) {
            categoryDto = new CategoryDto(
                    entity.getCategory().getId(),
                    entity.getCategory().getName(),
                    entity.getCategory().getDescription()
            );
        }

        return new ProductDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                categoryDto,
                entity.getImageUrl(),
                entity.getActive()
        );
    }

    public static Product mapToEntity(CreateProductRequest dto, Category category) {
        Product entity = new Product();

        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setCategory(category);
        entity.setImageUrl(dto.imageUrl());

        return entity;
    }
}
