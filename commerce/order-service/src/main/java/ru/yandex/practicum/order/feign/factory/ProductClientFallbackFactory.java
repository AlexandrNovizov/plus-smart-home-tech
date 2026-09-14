package ru.yandex.practicum.order.feign.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.order.exception.ProductServiceUnavailableException;
import ru.yandex.practicum.order.feign.ProductClient;

@Slf4j
@Component
public class ProductClientFallbackFactory implements FallbackFactory<ProductClient> {

    @Override
    public ProductClient create(Throwable cause) {
        return productId -> {
            log.warn(
                    "product-service недоступен при запросе товара id={}",
                    productId,
                    cause
            );

            throw new ProductServiceUnavailableException(productId, cause);
        };
    }
}
