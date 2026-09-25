package ru.yandex.practicum.order.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderData;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.OrderItemDto;
import ru.yandex.practicum.order.entity.Item;
import ru.yandex.practicum.order.entity.Order;
import ru.yandex.practicum.order.feign.dto.ProductDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@UtilityClass
public class OrderMapper {

    public static OrderData mapToOrderData(CreateOrderRequest request,
                                           List<OrderItemDto> items,
                                           boolean hasAllData,
                                           String statusDetails) {
        return new OrderData(
                request.customerName(),
                request.customerEmail(),
                items,
                hasAllData,
                statusDetails
        );
    }

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

    public static Order mapToEntity(OrderData data) {
        Order entity = new Order();

        entity.setCustomerEmail(data.customerEmail());
        entity.setCustomerName(data.customerName());
        if (data.hasAllData()) {
            entity.setStatus("CONFIRMED");
        } else {
            entity.setStatus("PENDING_CONFIRMATION");
        }
        entity.setStatusDetails(data.statusDetails());

        List<Item> items = data.items().stream()
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
