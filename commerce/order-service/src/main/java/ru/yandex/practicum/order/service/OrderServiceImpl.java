package ru.yandex.practicum.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderData;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.OrderItemRequest;
import ru.yandex.practicum.order.entity.Order;
import ru.yandex.practicum.order.exception.NotFoundException;
import ru.yandex.practicum.order.feign.InventoryClient;
import ru.yandex.practicum.order.feign.ProductClient;
import ru.yandex.practicum.order.feign.dto.ProductDto;
import ru.yandex.practicum.order.feign.dto.ReserveRequest;
import ru.yandex.practicum.order.feign.dto.ReserveResponse;
import ru.yandex.practicum.order.mapper.OrderMapper;
import ru.yandex.practicum.order.repository.OrderRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderDto> getAll() {

        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderDto create(OrderData data) {

        Order order = OrderMapper.mapToEntity(data);

        order = orderRepository.save(order);

        return OrderMapper.mapToDto(order);
    }

    @Override
    public OrderDto getById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Заказ с id=%d не найден", id))
        );
        return OrderMapper.mapToDto(order);
    }

    @Override
    public List<OrderDto> getByCustomerEmail(String customerEmail) {
        List<Order> orders = orderRepository.findByCustomerEmail(customerEmail);

        return orders.stream()
                .map(OrderMapper::mapToDto)
                .toList();
    }
}
