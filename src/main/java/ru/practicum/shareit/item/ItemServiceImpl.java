package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;

    @Override
    public ItemDto create(ItemDto dto, long userId) {
        User user = findUserByIdAndCheck(userId);
        Item newItem = ItemMapper.toItem(dto);
        newItem.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.save(newItem));
    }

    @Override
    public ItemDto update(ItemDto dto, long userId, long itemId) {
        findUserByIdAndCheck(userId);
        Item item = findItemByIdAndCheck(itemId);

        if (item.getOwner().getId() != userId) {
            log.warn("У пользователя с id={} нет прав на изменение вещи", userId);
            throw new AccessDeniedException("У пользователя с id=" + userId + " нет прав на изменение вещи");
        }

        if (dto.getName() != null) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getById(long itemId, long userId) {
        ItemDto itemDto = ItemMapper.toItemDto(findItemByIdAndCheck(itemId));
        addComments(itemDto);

        if (itemDto.getOwner().getId() == userId) {
            getItemWithBooking(itemDto);
        }

        return itemDto;
    }

    @Override
    public List<ItemDto> getByUserId(long userId) {
        return itemRepository.findAllByOwnerIdOrderByIdAsc(userId).stream()
                .map(ItemMapper::toItemDto)
                .map(this::getItemWithBooking)
                .map(this::addComments)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public CommentDto createComment(long userId, long itemId, CommentDto commentDto, LocalDateTime created) {
        if (commentDto.getText().isEmpty()) {
            throw new ValidationException("Текст отзывы не может быть пустым");
        }
        User user = findUserByIdAndCheck(userId);
        Item item = findItemByIdAndCheck(itemId);

        List<Booking> bookingsUserItems =
                bookingRepository.findBookingByItemIdAndBookerIdAndEndBeforeAndStatus(itemId, userId,
                        LocalDateTime.now(), BookingStatus.APPROVED);
        if (bookingsUserItems.isEmpty()) {
            throw new ValidationException("Пользователь " + userId + " не брал в аренду вещь " + itemId);
        }

        Comment newComment = ItemMapper.toComment(commentDto);
        newComment.setItem(item);
        newComment.setAuthor(user);
        newComment.setCreated(created);

        return ItemMapper.toCommentDto(commentRepository.save(newComment));
    }

    private Item findItemByIdAndCheck(long id) { // Возвращает item по ID и проверяет наличие в БД
        return itemRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Вещь с id " + id + " не найдена"));
    }

    private User findUserByIdAndCheck(long id) { // Возвращает юзера по ID и проверяет наличие в БД
        return userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь с id" + id + " не найден"));
    }

    private ItemDto getItemWithBooking(ItemDto itemDto) {
        List<Booking> lastBookings =
                bookingRepository.findAllByItemIdAndStartBeforeAndStatusOrderByStartDesc(itemDto.getId(),
                        LocalDateTime.now(), BookingStatus.APPROVED);
        if (!lastBookings.isEmpty()) {
            itemDto.setLastBooking(BookingMapper.toBookingItemDto(lastBookings.get(0)));
        }

        List<Booking> nextBookings =
                bookingRepository.findAllByItemIdAndStartAfterAndStatusOrderByStartAsc(itemDto.getId(),
                        LocalDateTime.now(), BookingStatus.APPROVED);
        if (!nextBookings.isEmpty()) {
            itemDto.setNextBooking(BookingMapper.toBookingItemDto(nextBookings.get(0)));
        }

        return itemDto;
    }
    private ItemDto addComments(ItemDto itemDto) {
        List<CommentDto> commentsDto = commentRepository.findAllByItemIdOrderByIdAsc(itemDto.getId())
                .stream()
                .map(ItemMapper::toCommentDto)
                .collect(Collectors.toList());

        itemDto.setComments(commentsDto);
        return itemDto;
    }
}