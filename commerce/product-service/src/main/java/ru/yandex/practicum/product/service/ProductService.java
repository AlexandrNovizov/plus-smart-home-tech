package ru.yandex.practicum.product.service;

import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.ProductDto;
import ru.yandex.practicum.product.dto.UpdateProductRequest;

import java.util.List;

public interface ProductService {

    List<ProductDto> getActive();

    ProductDto create(CreateProductRequest request);

    ProductDto getById(Long productId);

    ProductDto update(Long productId, UpdateProductRequest request);

    List<ProductDto> search(String query);

    List<ProductDto> getByCategory(Long categoryId);
}
