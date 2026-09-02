package ru.yandex.practicum.inventory.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.inventory.dto.InventoryDto;
import ru.yandex.practicum.inventory.dto.ReserveRequest;
import ru.yandex.practicum.inventory.dto.ReserveResponse;
import ru.yandex.practicum.inventory.dto.UpdateInventoryRequest;
import ru.yandex.practicum.inventory.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Validated
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public List<InventoryDto> getAll() {
        return inventoryService.getAll();
    }

    @GetMapping("/{productId}")
    public InventoryDto getByProductId(@PathVariable @Positive Long productId) {
        return inventoryService.getByProductId(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryDto create(@RequestBody @Valid UpdateInventoryRequest request) {
        return inventoryService.create(request);
    }

    @PostMapping("/reserve")
    public ReserveResponse reserve(@RequestBody @Valid ReserveRequest request) {
        return inventoryService.reserve(request);
    }

    @PutMapping
    public InventoryDto update(@RequestBody @Valid UpdateInventoryRequest request) {
        return inventoryService.update(request);
    }
}
