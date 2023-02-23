package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto dto, long userId);

    ItemDto update(ItemDto dto, long userId, long itemId);

    ItemDto getById(long id);

    List<ItemDto> getByUserId(long userId);

    List<ItemDto> search(String text);
}
