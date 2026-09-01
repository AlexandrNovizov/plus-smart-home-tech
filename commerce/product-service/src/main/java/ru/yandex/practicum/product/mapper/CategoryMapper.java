package ru.yandex.practicum.product.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.product.dto.CategoryDto;
import ru.yandex.practicum.product.dto.CreateCategoryRequest;
import ru.yandex.practicum.product.entity.Category;

@UtilityClass
public class CategoryMapper {

    public static CategoryDto mapToDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    public static Category mapToEntity(CreateCategoryRequest dto) {
        Category entity = new Category();

        entity.setName(dto.name());
        entity.setDescription(dto.description());

        return entity;
    }
}
