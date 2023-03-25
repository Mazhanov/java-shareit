package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.PageableCreate;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void getByIdTest() {
        Item item = getItem(1L);
        ItemDto itemDto = getItemDto(1L);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(commentRepository.findAllByItemIdOrderByIdAsc(anyLong()))
                .thenReturn(List.of());

        Assertions.assertEquals(itemService.getById(1, 2), itemDto);
    }

    @Test
    void getByIdTest_withBooking() {
        Item item = getItem(1L);
        ItemDto itemDto = getItemDto(1L);
        Booking lastBooking = getBooking(1L);
        Booking nextBooking = getBooking(2L);
        itemDto.setLastBooking(BookingMapper.toBookingItemDto(lastBooking));
        itemDto.setNextBooking(BookingMapper.toBookingItemDto(nextBooking));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(commentRepository.findAllByItemIdOrderByIdAsc(anyLong()))
                .thenReturn(List.of());
        when(bookingRepository.findAllByItemIdAndStartBeforeAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(lastBooking));
        when(bookingRepository.findAllByItemIdAndStartAfterAndStatusOrderByStartAsc(anyLong(), any(), any()))
                .thenReturn(List.of(nextBooking));

        Assertions.assertEquals(itemService.getById(1, 1), itemDto);
    }

    @Test
    void getByUserIdTest() {
        List<Item> items = List.of(getItem(1L));
        ItemDto itemDto = getItemDto(1L);
        List<ItemDto> itemsDto = List.of(getItemDto(1L));
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyLong(), any()))
                .thenReturn(items);
        when(commentRepository.findAllByItemIdOrderByIdAsc(anyLong()))
                .thenReturn(List.of());

        Assertions.assertEquals(itemService.getByUserId(1, PageableCreate.pageableCreate(1, 2)), itemsDto);
    }

    @Test
    void createTest() {
        ItemDto createItemDto = new ItemDto(null, "name", "description", false, null);
        createItemDto.setRequestId(1L);
        User user = new User(1L, "name", "email@email.ru");
        ItemDto itemDto = new ItemDto(1L, "name", "description", false, user);
        itemDto.setRequestId(1L);
        Item item = getItem(1L);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.save(any()))
                .thenReturn(item);

        Assertions.assertEquals(itemService.create(createItemDto, 1L), itemDto);
    }

    @Test
    void updateTest_withIncorrectUser() {
        ItemDto updateItemDto = getItemDto(1L);
        Item item = getItem(1L);
        User user = new User(1L, "name", "email@email.ru");
        item.setOwner(user);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        final AccessDeniedException exception = Assertions.assertThrows(
                AccessDeniedException.class,
                () -> itemService.update(updateItemDto, 2L, 1L));

        Assertions.assertEquals("У пользователя с id=2 нет прав на изменение вещи", exception.getMessage());
    }

    @Test
    void updateTest() {
        ItemDto updateItemDto = new ItemDto(1L, "testName", "description", false, null);
        Item item = getItem(1L);
        User user = new User(1L, "name", "email@email.ru");
        item.setName("testName");

        ItemDto itemDto = getItemDto(1L);
        itemDto.setName("testName");
        itemDto.setComments(null);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(itemRepository.save(any()))
                .thenReturn(item);

        Assertions.assertEquals(itemService.update(updateItemDto, 1L, 1L), itemDto);
    }

    @Test
    void createCommentTest_EmptyComment() {
        CommentDto commentDto = new CommentDto(1L, "", "author", LocalDateTime.now());

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> itemService.createComment(1L, 1L, commentDto, LocalDateTime.now()));

        Assertions.assertEquals("Текст отзывы не может быть пустым", exception.getMessage());
    }

    @Test
    void createCommentTest_noAccess() {
        CommentDto commentDto = new CommentDto(1L, "text", "author", LocalDateTime.now());
        Item item = getItem(1L);
        User user = new User(1L, "name", "email@email.ru");

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findBookingByItemIdAndBookerIdAndEndBeforeAndStatus(anyLong(),anyLong(), any(), any()))
                .thenReturn(List.of());

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> itemService.createComment(1L, 1L, commentDto, LocalDateTime.now()));

        Assertions.assertEquals("Пользователь 1 не брал в аренду вещь 1", exception.getMessage());
    }

    @Test
    void createCommentTest() {
        LocalDateTime date = LocalDateTime.now();
        CommentDto commentDto = new CommentDto(null, "text", "author", date);
        Item item = getItem(1L);
        User user = new User(1L, "name", "email@email.ru");

        Comment newComment = new Comment(1L, "text", item, user, date);

        CommentDto newCommentDto = new CommentDto(1L, "text", "name", date);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findBookingByItemIdAndBookerIdAndEndBeforeAndStatus(anyLong(),anyLong(), any(), any()))
                .thenReturn(List.of(new Booking()));
        when(commentRepository.save(any()))
                .thenReturn(newComment);

        Assertions.assertEquals(itemService.createComment(1L, 1L, commentDto, date), newCommentDto);
    }

    @Test
    void searchTest_textIsBlank() {
        Assertions.assertEquals(itemService.search("",PageableCreate.pageableCreate(1, 2)), List.of());
    }

    private Item getItem(long id) {
        User user = new User(1L, "name", "email@email.ru");
        return new Item(id, "name", "description", false, user, 1L);
    }

    private ItemDto getItemDto(long id) {
        User user = new User(1L, "name", "email@email.ru");
        ItemDto itemDto =  new ItemDto(id, "name", "description", false, user);
        itemDto.setRequestId(1L);
        itemDto.setComments(List.of());
        return itemDto;
    }

    private Booking getBooking(long id) {
        User user = new User(1L, "name", "email@email.ru");
        Item item = getItem(1L);
        return new Booking(id, LocalDateTime.now(), LocalDateTime.now(), item, user, BookingStatus.APPROVED);
    }
}