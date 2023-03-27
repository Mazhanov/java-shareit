package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {
    @Test
    void toItemDtoTest() {
        ItemRequest itemRequest = new ItemRequest(1L, "description", null, LocalDateTime.MIN);
        Item item = new Item(1L, "name", "description", false, null, itemRequest);

        ItemDto itemDto = new ItemDto(1L, "name", "description", false, null);
        itemDto.setRequestId(itemRequest.getId());

        assertEquals(ItemMapper.toItemDto(item), itemDto);
    }
}