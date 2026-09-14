package ru.yandex.practicum.order.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.OrderItemDto;
import ru.yandex.practicum.order.dto.OrderItemRequest;
import ru.yandex.practicum.order.exception.InventoryServiceUnavailableException;
import ru.yandex.practicum.order.exception.OrderProcessingException;
import ru.yandex.practicum.order.exception.ProductServiceUnavailableException;
import ru.yandex.practicum.order.feign.InventoryClient;
import ru.yandex.practicum.order.feign.ProductClient;
import ru.yandex.practicum.order.feign.ServiceCallResult;
import ru.yandex.practicum.order.feign.dto.ProductDto;
import ru.yandex.practicum.order.feign.dto.ReleaseRequest;
import ru.yandex.practicum.order.feign.dto.ReserveRequest;
import ru.yandex.practicum.order.mapper.ItemMapper;
import ru.yandex.practicum.order.mapper.OrderMapper;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderOrchestrationService {

    private final ProductClient productClient;
    private final InventoryClient inventoryClient;
    private final OrderService orderService;

    public OrderDto createOrder(CreateOrderRequest request) {
        Map<Long, Integer> productsQuantity = new HashMap<>();
        Map<Long, ProductDto> products = new HashMap<>();

        List<String> statusDetails = new LinkedList<>();

        boolean allProceed = getProducts(request, products, productsQuantity);
        boolean allReserved = reserveProducts(productsQuantity);

        if (allProceed) {
            statusDetails.add("Каталог временно недоступен");
        }

        if (allReserved) {
            statusDetails.add("Инвентарь временно недоступен");
        }

        boolean hasAllData = allProceed && allReserved;

        List<OrderItemDto> items = ItemMapper.mapToDto(products, productsQuantity);

        String finalDetails = String.join("; ", statusDetails);

        return orderService.create(OrderMapper.mapToOrderData(
                request,
                items,
                hasAllData,
                finalDetails.isBlank() ? null : finalDetails
        ));
    }

    private boolean reserveProducts(Map<Long, Integer> productsQuantity) {
        List<ReserveRequest> sentRequests = new LinkedList<>();
        boolean allReserved = true;

        for (Map.Entry<Long, Integer> entry : productsQuantity.entrySet()) {
            ReserveRequest reserveRequest = new ReserveRequest(entry.getKey(), entry.getValue());
            try {
                inventoryClient.reserveStock(reserveRequest);
                sentRequests.add(reserveRequest);
            } catch (FeignException e) {
                rollbackReserves(sentRequests);
                throw mapInventoryException(e, entry.getKey());
            } catch (InventoryServiceUnavailableException e) {
                allReserved = false;
            }
        }
        return allReserved;
    }

    private void rollbackReserves(List<ReserveRequest> sentRequests) {
        for (ReserveRequest sentRequest : sentRequests) {
            inventoryClient.releaseStock(
                    new ReleaseRequest(sentRequest.productId(), sentRequest.quantity())
            );
        }
    }

    private boolean getProducts(CreateOrderRequest request, Map<Long, ProductDto> products, Map<Long, Integer> productsQuantity) {
        boolean allProceeded = true;
        for (OrderItemRequest item : request.items()) {
            ProductDto product = getProductDto(item, products);
            if (product.name().contains("ожидает проверки")) {
                allProceeded = false;
            }

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
        return allProceeded;
    }

    private ProductDto getProductDto(OrderItemRequest item, Map<Long, ProductDto> products) {
        return products.compute(item.productId(), (productId, dto) -> {
                if (dto == null) {
                    return proceedProductResult(callProductService(productId), item);
                }
                return dto;
            }
        );
    }

    private ServiceCallResult<ProductDto> callProductService(Long productId) {
        try {
            return new ServiceCallResult.Success<>(productClient.getProductById(productId));
        } catch (FeignException e) {
            return mapProductException(e, productId);
        } catch (ProductServiceUnavailableException e) {
            return new ServiceCallResult.Degraded<>(
                    "Каталог временно недоступен: %s".formatted(e.getCause().getMessage())
            );
        }
    }

    private ServiceCallResult<ProductDto> mapProductException(FeignException exception, Long productId) {
        if (exception.status() == 404) {
            return new ServiceCallResult.Failure<>("Товар с id=%d не найден".formatted(productId));
        }

        if (exception.status() == 409) {
            return new ServiceCallResult.Failure<>("Данные уже существуют");
        }

        if (exception.status() == 400) {
            return new ServiceCallResult.Failure<>("Неверный запрос");
        }

        return new ServiceCallResult.Degraded<>("Не удалось получить данные товара: %s".formatted(exception.getMessage()));
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

    private ProductDto proceedProductResult(ServiceCallResult<ProductDto> result, OrderItemRequest item) {
        if (result instanceof ServiceCallResult.Success<ProductDto>) {
            return ((ServiceCallResult.Success<ProductDto>) result).value();
        } else if (result instanceof ServiceCallResult.Failure<ProductDto>) {
            throw new OrderProcessingException(((ServiceCallResult.Failure<ProductDto>) result).message());
        } else if (result instanceof ServiceCallResult.Degraded<ProductDto>) {
            String name = "Товар #<%d> (ожидает проверки)".formatted(item.productId());
            return new ProductDto(item.productId(), name, "ожидает проверки", BigDecimal.ZERO, true);
        }
        throw new IllegalArgumentException("Нет обработчика для ServiceCallResult." + result.getClass().getName());
    }
}
