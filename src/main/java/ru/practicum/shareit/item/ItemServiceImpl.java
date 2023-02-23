package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    @Override
    public ItemDto create(ItemDto dto, long userId) {
        User user = userRepository.getById(userId);
        Item newItem = ItemMapper.toItem(dto);
        return ItemMapper.toItemDto(itemRepository.create(newItem, user));
    }

    @Override
    public ItemDto update(ItemDto dto, long userId, long itemId) {
        checkPresenceItem(itemId);

        if (getById(itemId).getOwner().getId() != userId) {
            log.warn("У пользователя с id={} нет прав на изменение вещи", userId);
            throw new OtherUserException("У пользователя с id=" + userId + " нет прав на изменение вещи");
        }

        Item item = ItemMapper.toItem(dto);
        return ItemMapper.toItemDto(itemRepository.update(item, itemId));
    }

    @Override
    public ItemDto getById(long id) {
        return ItemMapper.toItemDto(itemRepository.getById(id));
    }

    @Override
    public List<ItemDto> getByUserId(long userId) {
        return itemRepository.getByUserId(userId).
                stream().map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.search(text).
                stream().map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void checkPresenceItem(long itemId) {
        getById(itemId);
    }
}