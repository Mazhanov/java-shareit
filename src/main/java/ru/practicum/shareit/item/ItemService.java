package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    Item create(ItemDto dto, long userId);

    Item update(ItemDto dto, long userId, long itemId);

    Item getById(long id);

    List<Item> getByUserId(long userId);

    List<Item> search(String text);
}
