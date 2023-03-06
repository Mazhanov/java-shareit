package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    public BookingDto createBooking(CreateBookingDto dto, long userId) {
        Item item = findItemByIdAndCheck(dto.getItemId());
        User booker = findUserByIdAndCheck(userId);

        if (!item.getAvailable()) {
            throw new ItemUnavailableException("Вещь " + item + " занята");
        }

        if (item.getOwner().getId() == userId) {
            throw new AccessDeniedException("Владелец вещи не может взять ее в аренду");
        }

        boolean startBeforeNow = dto.getStart().isBefore(LocalDateTime.now());
        boolean endBeforeNow = dto.getEnd().isBefore(LocalDateTime.now());
        boolean startAfterEnd = dto.getStart().isAfter(dto.getEnd());

        if (startBeforeNow || endBeforeNow || startAfterEnd) {
            throw new ValidationException("Ошибка валидации даты старта или финиша");
        }

        Booking booking = BookingMapper.toBooking(dto, item, booker);
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    public BookingDto changeStatusBooking(long userId, long bookingId, boolean status) {
        Booking booking = findBookingByIdAndCheck(bookingId);

        if (booking.getItem().getOwner().getId() != userId) {
            throw new AccessDeniedException("Вещь " + booking.getItem() + " не пренадлежит пользователю "
                    + userId);
        }

        if (booking.getStatus().equals(BookingStatus.APPROVED) || booking.getStatus().equals(BookingStatus.REJECTED)) {
            throw new ValidationException("Невозможно изменить статус бронирования " + bookingId);
        }

        if (status) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    public BookingDto getBooking(long userId, long bookingId) {
        Booking booking = findBookingByIdAndCheck(bookingId);

        if (booking.getItem().getOwner().getId() == userId || booking.getBooker().getId() == userId) {
            return BookingMapper.toBookingDto(booking);
        } else {
            throw new AccessDeniedException("Отказано в доступе  к бронированию пользователю " + userId +
                    "пользователь не создавал бронирование или вещь");
        }
    }

    public List<BookingDto> getAllByBooker(long userId, String state) {
        findUserByIdAndCheck(userId);

        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                                LocalDateTime.now(), LocalDateTime.now()).stream()
                                .map(BookingMapper::toBookingDto)
                                .collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()).stream()
                                .map(BookingMapper::toBookingDto)
                                .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now()).stream()
                                .map(BookingMapper::toBookingDto)
                                .collect(Collectors.toList());
            case "WAITING":
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING).stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            case "REJECTED":
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED).stream()
                                .map(BookingMapper::toBookingDto)
                                .collect(Collectors.toList());
            default:
                throw new UnsupportedStatusException();
        }
    }

    public List<BookingDto> getAllByOwner(long userId, String state) {
        findUserByIdAndCheck(userId);

        switch (state) {
            case "ALL":
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                                LocalDateTime.now(), LocalDateTime.now()).stream()
                                .map(BookingMapper::toBookingDto)
                                .collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()).stream()
                                .map(BookingMapper::toBookingDto)
                                .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now()).stream()
                                .map(BookingMapper::toBookingDto)
                                .collect(Collectors.toList());
            case "WAITING":
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING).stream()
                                .map(BookingMapper::toBookingDto)
                                .collect(Collectors.toList());
            case "REJECTED":
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED).stream()
                                .map(BookingMapper::toBookingDto)
                                .collect(Collectors.toList());
            default:
                throw new UnsupportedStatusException();
        }
    }

    private Booking findBookingByIdAndCheck(long id) { // Возвращает booking по ID и проверяет наличие в БД
        return bookingRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Бронирование с id " + id + " не найдена"));
    }

    private Item findItemByIdAndCheck(long id) { // Возвращает item по ID и проверяет наличие в БД
        return itemRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Вещь с id " + id + " не найдена"));
    }

    private User findUserByIdAndCheck(long id) { // Возвращает юзера по ID и проверяет наличие в БД
        return userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь с id" + id + " не найден"));
    }
}
