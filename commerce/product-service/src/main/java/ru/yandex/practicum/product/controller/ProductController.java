package ru.yandex.practicum.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.ProductDto;
import ru.yandex.practicum.product.dto.UpdateProductRequest;
import ru.yandex.practicum.product.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getActive() {
        return productService.getActive();
    }

    @GetMapping("/{id}")
    public ProductDto getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@RequestBody @Valid CreateProductRequest request) {
        return productService.create(request);
    }

    @PatchMapping("/{id}")
    public ProductDto update(@PathVariable Long id, @RequestBody @Valid UpdateProductRequest request) {
        return productService.update(id, request);
    }

    @GetMapping("/search")
    public List<ProductDto> search(@RequestParam(name = "query") String query) {
        return productService.search(query);
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductDto> getByCategory(@PathVariable Long categoryId) {
        return productService.getByCategory(categoryId);
    }

}
