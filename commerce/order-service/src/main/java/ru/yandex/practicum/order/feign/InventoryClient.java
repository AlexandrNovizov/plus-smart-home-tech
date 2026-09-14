package ru.yandex.practicum.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.order.feign.dto.ReleaseRequest;
import ru.yandex.practicum.order.feign.dto.ReleaseResponse;
import ru.yandex.practicum.order.feign.dto.ReserveRequest;
import ru.yandex.practicum.order.feign.dto.ReserveResponse;
import ru.yandex.practicum.order.feign.factory.InventoryClientFallbackFactory;

@FeignClient(
        name = "inventory-service",
        fallbackFactory = InventoryClientFallbackFactory.class
)
public interface InventoryClient {

    @PostMapping("/api/inventory/reserve")
    ReserveResponse reserveStock(@RequestBody ReserveRequest request);

    @PostMapping("/api/inventory/release")
    ReleaseResponse releaseStock(@RequestBody ReleaseRequest request);
}
