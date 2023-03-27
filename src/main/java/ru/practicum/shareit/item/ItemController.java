package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.PageableCreate;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private ItemService itemService;

    @PostMapping
    public ItemDto create(@Validated({Create.class}) @RequestBody ItemDto dto, @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        ItemDto item = itemService.create(dto, userId);
        log.info("Добавлена новая вещь {}", item);
        return item;
    }

    @PatchMapping("/{id}")
    public ItemDto update(@Validated({Update.class}) @RequestBody ItemDto dto,
                       @RequestHeader(name = "X-Sharer-User-Id") long userId,
                       @PathVariable long id) {
        ItemDto item = itemService.update(dto, userId, id);
        log.info("Обновлена вещь {}", item);
        return item;
    }

    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable long id, @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        ItemDto item = itemService.getById(id, userId);
        log.info("Возвращена вещь {}", item);
        return item;
    }

    @GetMapping
    public List<ItemDto> getByUserId(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                     @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                     @RequestParam(name = "size", defaultValue = "10") @Min(0) int size) {
        List<ItemDto> items = itemService.getByUserId(userId, PageableCreate.pageableCreate(from, size));
        log.info("Возвращен список вещей пользователя id={}, {}", userId, items);
        return items;
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text,
                                @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                @RequestParam(name = "size", defaultValue = "10") @Min(0) int size) {
        List<ItemDto> items = itemService.search(text, PageableCreate.pageableCreate(from, size));
        log.info("Возвращен список вещей по запросу {}, {}", text, items);
        return items;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(name = "X-Sharer-User-Id") long userId, @PathVariable long itemId,
                                    @RequestBody CommentDto commentDto) {
        CommentDto comment = itemService.createComment(userId, itemId, commentDto, LocalDateTime.now());
        log.info("Добавлен коммент к вещи {}, {}", itemId, comment);
        return comment;
    }
}