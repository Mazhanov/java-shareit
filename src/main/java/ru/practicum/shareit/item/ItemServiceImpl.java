package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private UserService userService;

    @Override
    public Item create(ItemDto dto, long userId) {
        User user = userService.getById(userId);
        Item newItem = ItemMapper.toItem(dto);
        return itemRepository.create(newItem, user);
    }

    @Override
    public Item update(ItemDto dto, long userId, long itemId) {
        checkPresenceItem(itemId);
        User user = userService.getById(userId);

        if (getById(itemId).getOwner().getId() != userId) {
            log.warn("У пользователя с id={} нет прав на изменение вещи", userId);
            throw new OtherUserException("У пользователя с id=" + userId + " нет прав на изменение вещи");
        }

        Item item = ItemMapper.toItem(dto);
        return itemRepository.update(item, itemId);
    }

    @Override
    public Item getById(long id) {
        return itemRepository.getById(id);
    }

    @Override
    public List<Item> getByUserId(long userId) {
        return itemRepository.getByUserId(userId);
    }

    @Override
    public List<Item> search(String text) {
        return itemRepository.search(text);
    }

    private void checkPresenceItem(long itemId) {
        getById(itemId);
    }
}