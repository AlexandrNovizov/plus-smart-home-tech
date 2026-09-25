package ru.yandex.practicum.order.service;

import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderData;
import ru.yandex.practicum.order.dto.OrderDto;

import java.util.List;

public interface OrderService {

    List<OrderDto> getAll();

    OrderDto create(OrderData data);

    OrderDto getById(Long id);

    List<OrderDto> getByCustomerEmail(String customerEmail);
}
