package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Slf4j
public class ItemController {
    private ItemService itemService;

    @PostMapping
    public Item create(@Validated({Create.class}) @RequestBody ItemDto dto, @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        Item item = itemService.create(dto, userId);
        log.info("Добавлена новая вещь {}", item);
        return item;
    }

    @PatchMapping("/{id}")
    public Item update(@Validated({Update.class}) @RequestBody ItemDto dto,
                       @RequestHeader(name = "X-Sharer-User-Id") long userId,
                       @PathVariable long id) {
        Item item = itemService.update(dto, userId, id);
        log.info("Обновлена вещь {}", item);
        return item;
    }

    @GetMapping("/{id}")
    public Item getById(@PathVariable long id) {
        Item item = itemService.getById(id);
        log.info("Возвращена вещь {}", item);
        return item;
    }

    @GetMapping
    public List<Item> getByUserId(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        List<Item> items = itemService.getByUserId(userId);
        log.info("Возвращен список вещей пользователя id={}, {}", userId, items);
        return items;
    }

    @GetMapping("/search")
    public List<Item> search(@RequestParam String text) {
        List<Item> items = itemService.search(text);
        log.info("Возвращен список вещей по запросу {}, {}", text, items);
        return items;
    }
}