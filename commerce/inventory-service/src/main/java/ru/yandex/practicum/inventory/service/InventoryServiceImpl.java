package ru.yandex.practicum.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.inventory.dto.*;
import ru.yandex.practicum.inventory.entity.Inventory;
import ru.yandex.practicum.inventory.exception.InsufficientStockException;
import ru.yandex.practicum.inventory.exception.NotEnoughReservedException;
import ru.yandex.practicum.inventory.exception.NotFoundException;
import ru.yandex.practicum.inventory.exception.RecordAlreadyExistsException;
import ru.yandex.practicum.inventory.mapper.InventoryMapper;
import ru.yandex.practicum.inventory.repository.InventoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public List<InventoryDto> getAll() {
        List<Inventory> records = inventoryRepository.findAll();

        return records.stream()
                .map(InventoryMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public InventoryDto update(UpdateInventoryRequest request) {

        Inventory record = inventoryRepository.findByProductId(request.productId()).orElseThrow(
                () -> new NotFoundException(
                        String.format("Инвентарная запись с id товара '%d' не найдена", request.productId())
                )
        );

        updateFields(record, request);
        inventoryRepository.save(record);
        return InventoryMapper.mapToDto(record);
    }

    @Override
    @Transactional
    public InventoryDto create(UpdateInventoryRequest request) {
        if (inventoryRepository.existsByProductId(request.productId())) {
            throw new RecordAlreadyExistsException(String.format(
                    "Инвентарная запись с id товара '%d' уже существует",
                    request.productId()
            ));
        }

        Inventory record = InventoryMapper.mapToEntity(request);
        record.setAvailableQuantity(record.getQuantity() - record.getReservedQuantity());
        record = inventoryRepository.save(record);
        return InventoryMapper.mapToDto(record);
    }

    @Override
    @Transactional
    public ReserveResponse reserve(ReserveRequest request) {
        Inventory record = inventoryRepository.findByProductId(request.productId()).orElseThrow(
                () -> new NotFoundException(
                        String.format("Инвентарная запись с id товара '%d' не найдена", request.productId())
                )
        );

        int totalReserved = record.getReservedQuantity() + request.quantity();

        if (record.getAvailableQuantity() < request.quantity()) {
            throw new NotEnoughReservedException(String.format(
                    "Недостаточно товара для резервирования, есть: %d, надо: %d",
                    record.getAvailableQuantity(), totalReserved
            ));
        }

        record.setReservedQuantity(totalReserved);
        record.setAvailableQuantity(record.getAvailableQuantity() - request.quantity());
        inventoryRepository.save(record);
        return new ReserveResponse(true, record.getAvailableQuantity(), "Товар успешно зарезервирован");
    }

    @Override
    public ReleaseResponse release(ReleaseRequest request) {
        Inventory record = inventoryRepository.findByProductId(request.productId()).orElseThrow(
                () -> new NotFoundException(
                        String.format("Инвентарная запись с id товара '%d' не найдена", request.productId())
                )
        );

        if (record.getReservedQuantity() < request.quantity()) {
            throw new InsufficientStockException(String.format(
                    "Недостаточно товара для снятия, зарезервировано: %d, надо: %d",
                    record.getReservedQuantity(), request.quantity())
            );
        }
        record.setReservedQuantity(record.getReservedQuantity() - request.quantity());
        record.setAvailableQuantity(record.getAvailableQuantity() + request.quantity());
        inventoryRepository.save(record);

        return new ReleaseResponse(true, record.getAvailableQuantity(), "Товар успешно снят");
    }

    @Override
    public InventoryDto getByProductId(Long productId) {
        Inventory record = inventoryRepository.findByProductId(productId).orElseThrow(
                () -> new NotFoundException(String.format("Инвентарная запись с id товара '%d' не найдена", productId))
        );

        return InventoryMapper.mapToDto(record);
    }

    private void updateFields(Inventory record, UpdateInventoryRequest request) {
        if (request.quantity() != null && !record.getQuantity().equals(request.quantity())) {
            if (request.quantity() < record.getReservedQuantity()) {
                throw new InsufficientStockException(
                        String.format("Общее кол-во товара меньше зарезервированного: %d < %d",
                                request.quantity(), record.getReservedQuantity())
                );
            }
            record.setQuantity(request.quantity());
            record.setAvailableQuantity(record.getQuantity() - record.getReservedQuantity());
        }
    }
}
