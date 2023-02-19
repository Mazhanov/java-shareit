package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    Item create(ItemCreateDto dto, long userId);

    Item update(ItemUpdateDto dto, long userId, long itemId);

    Item getById(long id);

    List<Item> getByUserId(long userId);

    List<Item> search(String text);
}
