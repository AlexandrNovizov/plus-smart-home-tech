package ru.yandex.practicum.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.ProductDto;
import ru.yandex.practicum.product.dto.UpdateProductRequest;
import ru.yandex.practicum.product.entity.Category;
import ru.yandex.practicum.product.entity.Product;
import ru.yandex.practicum.product.exception.DataAlreadyExistsException;
import ru.yandex.practicum.product.exception.NotFoundException;
import ru.yandex.practicum.product.mapper.ProductMapper;
import ru.yandex.practicum.product.repository.CategoryRepository;
import ru.yandex.practicum.product.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<ProductDto> getActive() {
        List<Product> products = productRepository.findByActiveTrue();

        return products.stream()
                .map(ProductMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public ProductDto create(CreateProductRequest request) {

        Category category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId()).orElseThrow(
                    () -> new NotFoundException(String.format("Категория с id=%d не найдена", request.categoryId()))
            );
        }

        if (productRepository.existsByName(request.name())) {
            throw new DataAlreadyExistsException("Товар с названием '%s' уже существует".formatted(request.name()));
        }

        Product product = ProductMapper.mapToEntity(request, category);

        product = productRepository.save(product);

        return ProductMapper.mapToDto(product);
    }

    @Override
    public ProductDto getById(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException(String.format("Товар с id=%d не найден", productId))
        );

        return ProductMapper.mapToDto(product);
    }

    @Override
    @Transactional
    public ProductDto update(Long productId, UpdateProductRequest request) {

        Category category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId()).orElseThrow(
                    () -> new NotFoundException(String.format("Категория с id=%d не найдена", request.categoryId()))
            );
        }

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException(String.format("Товар с id=%d не найден", productId))
        );

        if (!product.getName().equals(request.name()) && productRepository.existsByName(request.name())) {
            throw new DataAlreadyExistsException("Товар с названием '%s' уже существует".formatted(request.name()));
        }

        setFields(product, request, category);
        productRepository.save(product);
        return ProductMapper.mapToDto(product);
    }

    @Override
    public List<ProductDto> search(String query) {
        List<Product> products = productRepository.findByNameContaining(query);

        if (products.isEmpty()) {
            throw new NotFoundException("Товары не найдены");
        }

        return products.stream()
                .map(ProductMapper::mapToDto)
                .toList();
    }

    @Override
    public List<ProductDto> getByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);

        return products.stream()
                .map(ProductMapper::mapToDto)
                .toList();
    }

    private void setFields(Product product, UpdateProductRequest request, Category category) {
        if (request.name() != null && !request.name().equals(product.getName())) {
            product.setName(request.name());
        }
        if (request.description() != null && !request.description().equals(product.getDescription())) {
            product.setDescription(request.description());
        }
        if (request.price() != null && !request.price().equals(product.getPrice())) {
            product.setPrice(request.price());
        }
        if (category != null && !category.equals(product.getCategory())) {
            product.setCategory(category);
        }
        if (request.imageUrl() != null && !request.imageUrl().equals(product.getImageUrl())) {
            product.setImageUrl(request.imageUrl());
        }
        if (request.active() != null && !request.active().equals(product.getActive())) {
            product.setActive(request.active());
        }
    }
}
