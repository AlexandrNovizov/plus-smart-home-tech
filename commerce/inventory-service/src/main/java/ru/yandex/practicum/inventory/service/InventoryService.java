package ru.yandex.practicum.inventory.service;

import ru.yandex.practicum.inventory.dto.*;

import java.util.List;

public interface InventoryService {

    List<InventoryDto> getAll();

    InventoryDto update(UpdateInventoryRequest request);

    InventoryDto create(UpdateInventoryRequest request);

    ReserveResponse reserve(ReserveRequest request);

    ReleaseResponse release(ReleaseRequest request);

    InventoryDto getByProductId(Long productId);
}
