package ru.yandex.practicum.inventory.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.inventory.dto.InventoryDto;
import ru.yandex.practicum.inventory.dto.UpdateInventoryRequest;
import ru.yandex.practicum.inventory.entity.Inventory;

@UtilityClass
public class InventoryMapper {

    public static InventoryDto mapToDto(Inventory entity) {
        return new InventoryDto(
                entity.getId(),
                entity.getProductId(),
                entity.getQuantity(),
                entity.getReservedQuantity(),
                entity.getAvailableQuantity()
        );
    }

    public static Inventory mapToEntity(UpdateInventoryRequest request) {
        Inventory entity = new Inventory();

        entity.setProductId(request.productId());
        entity.setQuantity(request.quantity());

        return entity;
    }
}
