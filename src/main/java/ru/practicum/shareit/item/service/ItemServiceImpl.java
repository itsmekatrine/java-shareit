package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(ItemDto dto, Long userId) {
        Item item = ItemMapper.toModel(dto);
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден: " + userId));
        item.setOwner(owner);
        return ItemMapper.toDto(repository.save(item));
    }

    @Override
    public ItemDto update(Long itemId, ItemDto dto, Long userId) {
        Item existing = repository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Вещь не найдена: " + itemId));
        if (!existing.getOwner().equals(userId)) {
            throw new NoSuchElementException("Только владелец может редактировать вещь: " + itemId);
        }
        Item updateItem = ItemMapper.toModel(dto);
        updateItem.setId(itemId);

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден: " + userId));
        updateItem.setOwner(owner);
        return ItemMapper.toDto(repository.update(updateItem));
    }

    @Override
    public Optional<ItemDto> getById(Long itemId, Long userId) {
        return repository.findById(itemId)
                .map(ItemMapper::toDto);
    }

    @Override
    public List<ItemDto> getAllByUser(Long userId) {
        return repository.findAll().stream()
                .filter(item -> item.getOwner().equals(userId))
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        String s = text.toLowerCase();
        return repository.findAll().stream()
                .filter(Item::getAvailable)
                .filter(item ->
                        item.getName().toLowerCase().contains(s) ||
                                item.getDescription().toLowerCase().contains(s)
                )
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
