package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    ItemRepository itemRepository;
    UserService userService;

    @Override
    public Item create(ItemCreateDto dto, long userId) {
        User user = userService.getById(userId);
        Item newItem = ItemMapper.toItemCreate(dto);
        return itemRepository.create(newItem, user);
    }

    @Override
    public Item update(ItemUpdateDto dto, long userId, long itemId) {
        checkPresenceItem(itemId);

        if (getById(itemId).getOwner().getId() != userId) {
            log.warn("У пользователя с id={} нет прав на изменение вещи", userId);
            throw new OtherUserException("У пользователя с id=" + userId + " нет прав на изменение вещи");
        }

        Item item = ItemMapper.toItemUpdate(dto);
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
