package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Slf4j
public class ItemController {
    ItemService itemService;

    @PostMapping
    public Item create(@Valid @RequestBody ItemCreateDto dto, @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        Item item = itemService.create(dto, userId);
        log.info("Добавлена новая вещь {}", item);
        return item;
    }

    @PatchMapping("/{id}")
    public Item update(@Valid @RequestBody ItemUpdateDto dto,
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
