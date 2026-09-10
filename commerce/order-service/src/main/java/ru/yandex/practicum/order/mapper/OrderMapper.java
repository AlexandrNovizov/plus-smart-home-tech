package ru.yandex.practicum.order.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.OrderItemDto;
import ru.yandex.practicum.order.entity.Item;
import ru.yandex.practicum.order.entity.Order;

import java.math.BigDecimal;
import java.util.List;

@UtilityClass
public class OrderMapper {

    public static OrderDto mapToDto(Order entity) {

        List<OrderItemDto> itemDtos = entity.getItems().stream()
                .map(ItemMapper::mapToDto)
                .toList();

        return new OrderDto(
                entity.getId(),
                entity.getCustomerName(),
                entity.getCustomerEmail(),
                entity.getStatus(),
                entity.getTotalPrice(),
                entity.getStatusDetails(),
                entity.getCreatedAt(),
                itemDtos
        );
    }

    public static Order mapToEntity(CreateOrderRequest request) {
        Order entity = new Order();

        entity.setCustomerEmail(request.customerEmail());
        entity.setCustomerName(request.customerName());
        entity.setStatus("CREATED");

        List<Item> items = request.items().stream()
                .map(ItemMapper::mapToEntity)
                .toList();

        entity.setItems(items);

        BigDecimal totalPrice = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        entity.setTotalPrice(totalPrice);

        return entity;
    }
}
