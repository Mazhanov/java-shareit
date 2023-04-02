package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.PageableCreate;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void createBookingTest_unavailableItem() {
        CreateBookingDto createBookingDto = makeCreateBookingDto();
        Item item = makeItem(1L);
        User user = makeUser(1L);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        final ItemUnavailableException exception = Assertions.assertThrows(
                ItemUnavailableException.class,
                () -> bookingService.createBooking(createBookingDto, 1L));

        Assertions.assertEquals("Вещь Item(id=1, name=name, description=description, " +
                "available=false, owner=null, request=null) занята", exception.getMessage());
    }

    @Test
    void createBookingTest_OwnerNotCanItem() {
        CreateBookingDto createBookingDto = makeCreateBookingDto();
        Item item = makeItem(1L);
        item.setAvailable(true);
        User user = makeUser(1L);
        item.setOwner(user);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(createBookingDto, 1L));

        Assertions.assertEquals("Владелец вещи не может взять ее в аренду", exception.getMessage());
    }

    /*@Test
    void createBookingTest_startAfterEnd() {
        CreateBookingDto createBookingDto = makeCreateBookingDto();
        Item item = makeItem(1L);
        item.setAvailable(true);
        item.setOwner(makeUser(100L));
        User user = makeUser(1L);
        createBookingDto.setEnd(LocalDateTime.now().plusDays(2));
        createBookingDto.setStart(LocalDateTime.MAX);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(createBookingDto, 1L));

        Assertions.assertEquals("Ошибка валидации даты старта или финиша", exception.getMessage());
    }*/

    /*@Test
    void createBookingTest_startBeforeNow() {
        CreateBookingDto createBookingDto = makeCreateBookingDto();
        Item item = makeItem(1L);
        item.setAvailable(true);
        item.setOwner(makeUser(100L));
        User user = makeUser(1L);
        createBookingDto.setStart(LocalDateTime.now().minusDays(2));

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(createBookingDto, 1L));

        Assertions.assertEquals("Ошибка валидации даты старта или финиша", exception.getMessage());
    }*/

    /*@Test
    void createBookingTest_endBeforeNow() {
        CreateBookingDto createBookingDto = makeCreateBookingDto();
        Item item = makeItem(1L);
        item.setAvailable(true);
        item.setOwner(makeUser(100L));
        User user = makeUser(1L);
        createBookingDto.setEnd(LocalDateTime.now().minusDays(2));

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(createBookingDto, 1L));

        Assertions.assertEquals("Ошибка валидации даты старта или финиша", exception.getMessage());
    }*/

    /*@Test
    void createBookingTest_startEqualEnd() {
        CreateBookingDto createBookingDto = makeCreateBookingDto();
        Item item = makeItem(1L);
        item.setAvailable(true);
        item.setOwner(makeUser(100L));
        User user = makeUser(1L);
        LocalDateTime date = LocalDateTime.now().plusDays(2);
        createBookingDto.setEnd(date);
        createBookingDto.setStart(date);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(createBookingDto, 1L));

        Assertions.assertEquals("Ошибка валидации даты старта или финиша", exception.getMessage());
    }*/

    @Test
    void createBookingTest() {
        CreateBookingDto createBookingDto = makeCreateBookingDto();
        Item item = makeItem(1L);
        item.setAvailable(true);
        item.setOwner(makeUser(100L));
        User user = makeUser(1L);

        Booking booking = BookingMapper.toBooking(createBookingDto, item, user);
        booking.setId(1L);
        BookingDto bookingDto = BookingMapper.toBookingDto(booking);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(bookingRepository.save(any()))
                .thenReturn(booking);

        Assertions.assertEquals(bookingService.createBooking(createBookingDto, 1L), bookingDto);
    }

    @Test
    void changeStatusBookingTest_accessDenied() {
        Booking booking = makeBooking(1L);
        Item item = makeItem(1L);
        item.setAvailable(true);
        item.setOwner(makeUser(100L));
        booking.setItem(item);

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        final AccessDeniedException exception = Assertions.assertThrows(
                AccessDeniedException.class,
                () -> bookingService.changeStatusBooking(1L, 1L, true));

        Assertions.assertEquals("Вещь Item(id=1, name=name, description=description, available=true, " +
                "owner=User(id=100, name=name, email=email@email.ru), request=null) " +
                "не пренадлежит пользователю 1", exception.getMessage());
    }

    @Test
    void changeStatusBookingTest_incorrectStatusRejected() {
        Booking booking = makeBooking(1L);
        Item item = makeItem(1L);
        item.setAvailable(true);
        item.setOwner(makeUser(1L));
        booking.setItem(item);
        booking.setStatus(BookingStatus.REJECTED);

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.changeStatusBooking(1L, 1L, true));

        Assertions.assertEquals("Невозможно изменить статус бронирования 1", exception.getMessage());
    }

    @Test
    void changeStatusBookingTest_incorrectStatusApproved() {
        Booking booking = makeBooking(1L);
        Item item = makeItem(1L);
        item.setAvailable(true);
        item.setOwner(makeUser(1L));
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.changeStatusBooking(1L, 1L, true));

        Assertions.assertEquals("Невозможно изменить статус бронирования 1", exception.getMessage());
    }

    @Test
    void changeStatusBookingTest_approved() {
        LocalDateTime start = LocalDateTime.now().plusDays(100);
        LocalDateTime end = LocalDateTime.MAX;
        Item item = makeItem(1L);
        item.setAvailable(true);
        item.setOwner(makeUser(1L));

        Booking booking = makeBooking(1L);
        booking.setItem(item);
        booking.setStart(start);
        booking.setEnd(end);

        Booking updateBooking = makeBooking(1L);
        updateBooking.setItem(item);
        updateBooking.setStatus(BookingStatus.APPROVED);
        updateBooking.setStart(start);
        updateBooking.setEnd(end);
        BookingDto updateBookingDto = BookingMapper.toBookingDto(updateBooking);

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.save(any()))
                .thenReturn(booking);

        Assertions.assertEquals(bookingService.changeStatusBooking(1L, 1L, true), updateBookingDto);
    }

    @Test
    void changeStatusBookingTest_rejected() {
        LocalDateTime start = LocalDateTime.now().plusDays(100);
        LocalDateTime end = LocalDateTime.MAX;
        Item item = makeItem(1L);
        item.setAvailable(true);
        item.setOwner(makeUser(1L));

        Booking booking = makeBooking(1L);
        booking.setItem(item);
        booking.setStart(start);
        booking.setEnd(end);

        Booking updateBooking = makeBooking(1L);
        updateBooking.setItem(item);
        updateBooking.setStatus(BookingStatus.REJECTED);
        updateBooking.setStart(start);
        updateBooking.setEnd(end);
        BookingDto updateBookingDto = BookingMapper.toBookingDto(updateBooking);

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.save(any()))
                .thenReturn(booking);

        Assertions.assertEquals(bookingService.changeStatusBooking(1L, 1L, false), updateBookingDto);
    }

    @Test
    void changeStatusBookingTest_BookingNotFound() {
        when(bookingRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException("not"));

        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> bookingService.changeStatusBooking(1L, 1L, false));

        Assertions.assertEquals("not", exception.getMessage());
    }

    @Test
    void getBookingTest_accessDenied() {
        Booking booking = makeBooking(1L);
        Item item = makeItem(1L);
        item.setAvailable(true);
        item.setOwner(makeUser(1L));
        booking.setItem(item);
        booking.setBooker(makeUser(2L));

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        final AccessDeniedException exception = Assertions.assertThrows(
                AccessDeniedException.class,
                () -> bookingService.getBooking(100L, 1L));

        Assertions.assertEquals("Отказано в доступе  к бронированию пользователю 100" +
                "пользователь не создавал бронирование или вещь", exception.getMessage());
    }

    @Test
    void getBookingTest_byOwner() {
        Booking booking = makeBooking(1L);
        Item item = makeItem(1L);
        item.setAvailable(true);
        item.setOwner(makeUser(1L));
        booking.setItem(item);
        booking.setBooker(makeUser(2L));

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        Assertions.assertEquals(bookingService.getBooking(1L, 1L), BookingMapper.toBookingDto(booking));
    }

    @Test
    void getBookingTest_byBooker() {
        Booking booking = makeBooking(1L);
        Item item = makeItem(1L);
        item.setAvailable(true);
        item.setOwner(makeUser(2L));
        booking.setItem(item);
        booking.setBooker(makeUser(1L));

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        Assertions.assertEquals(bookingService.getBooking(1L, 1L), BookingMapper.toBookingDto(booking));
    }

    @Test
    void getAllByBookerTest_all() {
        List<Booking> bookings = List.of(makeBooking(1L), makeBooking(2L));
        List<BookingDto> bookingsDto = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(makeUser(1L)));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(bookings);

        Assertions.assertEquals(bookingService.getAllByBooker(1L,
                BookingState.ALL, PageableCreate.pageableCreate(1, 10)), bookingsDto);
    }

    @Test
    void getAllByBookerTest_current() {
        List<Booking> bookings = List.of(makeBooking(1L), makeBooking(2L));
        List<BookingDto> bookingsDto = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(makeUser(1L)));
        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(), any(),any()))
                .thenReturn(bookings);

        Assertions.assertEquals(bookingService.getAllByBooker(1L,
                BookingState.CURRENT, PageableCreate.pageableCreate(1, 10)), bookingsDto);
    }

    @Test
    void getAllByBookerTest_past() {
        List<Booking> bookings = List.of(makeBooking(1L), makeBooking(2L));
        List<BookingDto> bookingsDto = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(makeUser(1L)));
        when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookings);

        Assertions.assertEquals(bookingService.getAllByBooker(1L,
                BookingState.PAST, PageableCreate.pageableCreate(1, 10)), bookingsDto);
    }

    @Test
    void getAllByBookerTest_future() {
        List<Booking> bookings = List.of(makeBooking(1L), makeBooking(2L));
        List<BookingDto> bookingsDto = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(makeUser(1L)));
        when(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookings);

        Assertions.assertEquals(bookingService.getAllByBooker(1L,
                BookingState.FUTURE, PageableCreate.pageableCreate(1, 10)), bookingsDto);
    }

    @Test
    void getAllByBookerTest_waiting() {
        List<Booking> bookings = List.of(makeBooking(1L), makeBooking(2L));
        List<BookingDto> bookingsDto = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(makeUser(1L)));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookings);

        Assertions.assertEquals(bookingService.getAllByBooker(1L,
                BookingState.WAITING, PageableCreate.pageableCreate(1, 10)), bookingsDto);
    }

    @Test
    void getAllByBookerTest_rejected() {
        List<Booking> bookings = List.of(makeBooking(1L), makeBooking(2L));
        List<BookingDto> bookingsDto = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(makeUser(1L)));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookings);

        Assertions.assertEquals(bookingService.getAllByBooker(1L,
                BookingState.REJECTED, PageableCreate.pageableCreate(1, 10)), bookingsDto);
    }

    @Test
    void getAllByOwnerTest_all() {
        List<Booking> bookings = List.of(makeBooking(1L), makeBooking(2L));
        List<BookingDto> bookingsDto = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(makeUser(1L)));
        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(bookings);

        Assertions.assertEquals(bookingService.getAllByOwner(1L,
                BookingState.ALL, PageableCreate.pageableCreate(1, 10)), bookingsDto);
    }

    @Test
    void getAllByOwnerTest_current() {
        List<Booking> bookings = List.of(makeBooking(1L), makeBooking(2L));
        List<BookingDto> bookingsDto = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(makeUser(1L)));
        when(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(), any(), any()))
                .thenReturn(bookings);

        Assertions.assertEquals(bookingService.getAllByOwner(1L,
                BookingState.CURRENT, PageableCreate.pageableCreate(1, 10)), bookingsDto);
    }

    @Test
    void getAllByOwnerTest_past() {
        List<Booking> bookings = List.of(makeBooking(1L), makeBooking(2L));
        List<BookingDto> bookingsDto = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(makeUser(1L)));
        when(bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookings);

        Assertions.assertEquals(bookingService.getAllByOwner(1L,
                BookingState.PAST, PageableCreate.pageableCreate(1, 10)), bookingsDto);
    }

    @Test
    void getAllByOwnerTest_future() {
        List<Booking> bookings = List.of(makeBooking(1L), makeBooking(2L));
        List<BookingDto> bookingsDto = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(makeUser(1L)));
        when(bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookings);

        Assertions.assertEquals(bookingService.getAllByOwner(1L,
                BookingState.FUTURE, PageableCreate.pageableCreate(1, 10)), bookingsDto);
    }

    @Test
    void getAllByOwnerTest_waiting() {
        List<Booking> bookings = List.of(makeBooking(1L), makeBooking(2L));
        List<BookingDto> bookingsDto = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(makeUser(1L)));
        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookings);

        Assertions.assertEquals(bookingService.getAllByOwner(1L,
                BookingState.WAITING, PageableCreate.pageableCreate(1, 10)), bookingsDto);
    }

    @Test
    void getAllByOwnerTest_rejected() {
        List<Booking> bookings = List.of(makeBooking(1L), makeBooking(2L));
        List<BookingDto> bookingsDto = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(makeUser(1L)));
        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookings);

        Assertions.assertEquals(bookingService.getAllByOwner(1L,
                BookingState.REJECTED, PageableCreate.pageableCreate(1, 10)), bookingsDto);
    }

    private CreateBookingDto makeCreateBookingDto() {
        return new CreateBookingDto(null, LocalDateTime.now().plusDays(100), LocalDateTime.MAX,
                1L, 1L, BookingStatus.WAITING);
    }

    private Booking makeBooking(long id) {
        return new Booking(id, LocalDateTime.now().plusDays(100), LocalDateTime.MAX,null, null,
                BookingStatus.WAITING);
    }

    private Item makeItem(long id) {
        return new Item(id, "name", "description", false, null, null);
    }

    private User makeUser(long id) {
        return new User(id, "name", "email@email.ru");
    }
}