package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ItemRepository {
    private Map<Long, Item> items = new HashMap<>();
    private long itemId = 0;

    public Item create(Item item, User user) {
        generateItemId(item);
        item.setOwner(user);
        items.put(item.getId(), item);
        return item;
    }

    public Item update(Item item, long itemId) {
        if (item.getName() != null) {
            items.get(itemId).setName(item.getName());
        }
        if (item.getDescription() != null) {
            items.get(itemId).setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            items.get(itemId).setAvailable(item.getAvailable());
        }
        return items.get(itemId);
    }

    public Item getById(long id) {
        checkPresenceItem(id);
        return items.get(id);
    }

    public List<Item> getByUserId(long userId) {
        return items.values().stream()
                .filter(x -> x.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    public List<Item> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }

        return items.values().stream()
                .filter(x -> (x.getDescription().toLowerCase().contains(text.toLowerCase()))
                        || x.getName().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    private void generateItemId(Item item) {
        itemId++;
        item.setId(itemId);
    }

    private void checkPresenceItem(long itemId) {
        if (!items.containsKey(itemId)) {
            log.warn("Вещь с id {} не найдена", itemId);
            throw new ObjectNotFoundException("Вещь с id " + itemId + " не найдена");
        }
    }
}
