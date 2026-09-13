package ru.yandex.practicum.order.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.OrderItemDto;
import ru.yandex.practicum.order.dto.OrderItemRequest;
import ru.yandex.practicum.order.exception.OrderProcessingException;
import ru.yandex.practicum.order.feign.InventoryClient;
import ru.yandex.practicum.order.feign.ProductClient;
import ru.yandex.practicum.order.feign.dto.ProductDto;
import ru.yandex.practicum.order.feign.dto.ReleaseRequest;
import ru.yandex.practicum.order.feign.dto.ReserveRequest;
import ru.yandex.practicum.order.mapper.ItemMapper;
import ru.yandex.practicum.order.mapper.OrderMapper;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderOrchestrationService {

    private final ProductClient productClient;
    private final InventoryClient inventoryClient;
    private final OrderService orderService;

    public OrderDto createOrder(CreateOrderRequest request) {
        Map<Long, Integer> productsQuantity = new HashMap<>();
        Map<Long, ProductDto> products = new HashMap<>();

        getProductsQuantity(request, products, productsQuantity);
        reserveProducts(productsQuantity);

        List<OrderItemDto> items = ItemMapper.mapToDto(products, productsQuantity);

        return orderService.create(OrderMapper.mapToOrderData(request, items));
    }

    private void reserveProducts(Map<Long, Integer> productsQuantity) {
        List<ReserveRequest> sentRequests = new LinkedList<>();

        for (Map.Entry<Long, Integer> entry : productsQuantity.entrySet()) {
            ReserveRequest reserveRequest = new ReserveRequest(entry.getKey(), entry.getValue());
            try {
                inventoryClient.reserveStock(reserveRequest);
                sentRequests.add(reserveRequest);
            } catch (FeignException e) {
                rollbackReserves(sentRequests);
                throw mapInventoryException(e, entry.getKey());
            }
        }
    }

    private void rollbackReserves(List<ReserveRequest> sentRequests) {
        for (ReserveRequest sentRequest : sentRequests) {
            inventoryClient.releaseStock(
                    new ReleaseRequest(sentRequest.productId(), sentRequest.quantity())
            );
        }
    }

    private void getProductsQuantity(CreateOrderRequest request, Map<Long, ProductDto> products, Map<Long, Integer> productsQuantity) {
        for (OrderItemRequest item : request.items()) {
            ProductDto product = getProductDto(item, products);

            if (!product.active()) {
                throw new OrderProcessingException("Товар снят с продажи");
            }

            productsQuantity.compute(item.productId(), (productId, quantity) -> {
                if (quantity == null) {
                    return item.quantity();
                }
                return quantity + item.quantity();
            });
        }
    }

    private ProductDto getProductDto(OrderItemRequest item, Map<Long, ProductDto> products) {
        try {
            return products.compute(item.productId(), (productId, dto) -> {
                if (dto == null) {
                    return productClient.getProductById(productId);
                }
                return dto;
            });
        } catch (FeignException e) {
            throw mapProductException(e, item.productId());
        }
    }

    private OrderProcessingException mapProductException(FeignException exception, Long productId) {
        if (exception.status() == 404) {
            return new OrderProcessingException("Товар с id=%d не найден".formatted(productId));
        }

        return new OrderProcessingException("Не удалось получить данные товара");
    }

    private OrderProcessingException mapInventoryException(FeignException exception, Long productId) {
        if (exception.status() == 404) {
            return new OrderProcessingException("Складская запись для товара id=%d не найдена".formatted(productId));
        }

        if (exception.status() == 409) {
            return new OrderProcessingException("Недостаточно товара id=%d на складе".formatted(productId));
        }

        return new OrderProcessingException("Не удалось зарезервировать товар");
    }
}
