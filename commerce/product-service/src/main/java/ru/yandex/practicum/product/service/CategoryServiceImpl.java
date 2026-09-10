package ru.yandex.practicum.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.product.dto.CategoryDto;
import ru.yandex.practicum.product.dto.CreateCategoryRequest;
import ru.yandex.practicum.product.entity.Category;
import ru.yandex.practicum.product.exception.DataAlreadyExistsException;
import ru.yandex.practicum.product.exception.NotFoundException;
import ru.yandex.practicum.product.mapper.CategoryMapper;
import ru.yandex.practicum.product.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAll() {

        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(CategoryMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public CategoryDto create(CreateCategoryRequest request) {

        Category category = CategoryMapper.mapToEntity(request);

        if (categoryRepository.existsByName(request.name())) {
            throw new DataAlreadyExistsException("Категория с названием '%s' уже существует".formatted(request.name()));
        }
        category = categoryRepository.save(category);

        return CategoryMapper.mapToDto(category);
    }

    @Override
    public CategoryDto getById(Long id) {

        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Категория с id=%d не найдена", id))
        );

        return CategoryMapper.mapToDto(category);
    }
}
