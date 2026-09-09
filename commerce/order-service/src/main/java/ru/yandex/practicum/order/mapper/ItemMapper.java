package ru.yandex.practicum.order.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.order.dto.OrderItemDto;
import ru.yandex.practicum.order.dto.OrderItemRequest;
import ru.yandex.practicum.order.entity.Item;

@UtilityClass
public class ItemMapper {

    public static OrderItemDto mapToDto(Item entity) {
        return new OrderItemDto(
                entity.getId(),
                entity.getProductId(),
                entity.getProductName(),
                entity.getQuantity(),
                entity.getPrice()
        );
    }

    public static Item mapToEntity(OrderItemRequest request) {
        Item entity = new Item();

        entity.setProductId(request.productId());
        entity.setProductName(request.productName());
        entity.setQuantity(request.quantity());
        entity.setPrice(request.price());

        return entity;
    }
}
