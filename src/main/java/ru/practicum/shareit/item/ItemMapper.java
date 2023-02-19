package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

public class ItemMapper {
    public static Item toItemCreate(ItemCreateDto dto) {
        return new Item(dto.getName(), dto.getDescription(), dto.getAvailable());
    }

    public static Item toItemUpdate(ItemUpdateDto dto) {
        Item item = new Item();
        if (dto.getName() != null) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
        return item;
    }
}
