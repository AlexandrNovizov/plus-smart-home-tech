package ru.yandex.practicum.order.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.order.dto.OrderItemDto;
import ru.yandex.practicum.order.entity.Item;
import ru.yandex.practicum.order.feign.dto.ProductDto;

import java.util.List;
import java.util.Map;

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

    public static Item mapToEntity(OrderItemDto dto) {
        Item entity = new Item();

        entity.setId(dto.id());
        entity.setProductName(dto.productName());
        entity.setProductId(dto.productId());
        entity.setPrice(dto.price());
        entity.setQuantity(dto.quantity());

        return entity;
    }

    public static List<OrderItemDto> mapToDto(Map<Long, ProductDto> products, Map<Long, Integer> quantity) {
        return products.entrySet().stream()
                .map(entry -> mapToDto(entry.getValue(), quantity.get(entry.getKey())))
                .toList();
    }

    public static OrderItemDto mapToDto(ProductDto productDto, Integer quantity) {
        return new OrderItemDto(
                null,
                productDto.id(),
                productDto.name(),
                quantity,
                productDto.price()
        );
    }
}
