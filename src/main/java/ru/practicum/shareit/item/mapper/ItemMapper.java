package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto toDto(Item item) {
        return toDto(item, null, null);
    }

    // Item → ItemDto
    public static ItemDto toDto(Item item, BookingForItemDto lastBooking, BookingForItemDto nextBooking) {
        if (item == null) {
            return null;
        }
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest(),
                lastBooking,
                nextBooking
        );
    }

    // ItemDto → Item
    public static Item toModel(ItemDto dto) {
        if (dto == null) {
            return null;
        }
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return item;
    }
}
