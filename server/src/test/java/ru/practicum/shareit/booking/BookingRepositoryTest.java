package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.PageableCreate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private final User user = new User(1L, "name1", "email1@email.ru");
    private final User user2 = new User(2L, "name2", "email2@email.ru");
    private final User user3 = new User(3L, "name3", "email3@email.ru");

    private final Item item1 = new Item(1L, "name1", "description1", true, user, null);
    private final Item item2 = new Item(2L, "name2", "description2", true, user2, null);
    private final Item item3 = new Item(3L, "name3", "description3", true, user3, null);

    private final Pageable pageable = PageableCreate.pageableCreate(1, 10);

    @BeforeEach
    void setup() {
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
    }

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    void findAllByBookerIdTest() {
        Booking booking = new Booking(1L, LocalDateTime.MIN, LocalDateTime.MAX, item1, user, BookingStatus.REJECTED);
        Booking booking2 = new Booking(2L, LocalDateTime.MIN, LocalDateTime.MAX, item1, user2, BookingStatus.REJECTED);
        Booking booking3 = new Booking(3L, LocalDateTime.MIN, LocalDateTime.MAX, item1, user, BookingStatus.REJECTED);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);

        Assertions.assertEquals(bookingRepository.findAllByBookerIdOrderByStartDesc(1, pageable), List.of(booking, booking3));
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfter_test() {
        Booking booking = new Booking(1L, LocalDateTime.now().plusDays(11), LocalDateTime.MAX, item1, user, BookingStatus.REJECTED);
        Booking booking2 = new Booking(2L, LocalDateTime.MIN, LocalDateTime.now().minusDays(11), item1, user, BookingStatus.REJECTED);
        Booking booking3 = new Booking(3L, LocalDateTime.now().minusDays(11), LocalDateTime.now().plusDays(11), item1, user, BookingStatus.REJECTED);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);

        System.out.println(userRepository.findAll());
        System.out.println(itemRepository.findAll());
        System.out.println(bookingRepository.findAll());
        Assertions.assertEquals(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(1,
                LocalDateTime.now(), LocalDateTime.now(), pageable), List.of(booking3));
    }

    @Test
    void findAllByBookerIdAndEndBeforeOrderByStartDesc_test() {
        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(30), LocalDateTime.now().minusDays(15), item1, user, BookingStatus.REJECTED);
        Booking booking2 = new Booking(2L, LocalDateTime.now().minusDays(15), LocalDateTime.now().minusDays(11), item1, user, BookingStatus.REJECTED);
        Booking booking3 = new Booking(3L, LocalDateTime.now().minusDays(15), LocalDateTime.now().plusDays(15), item1, user, BookingStatus.REJECTED);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);

        Assertions.assertEquals(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(1, LocalDateTime.now(),
                pageable), List.of(booking2, booking));
    }

    @Test
    void findAllByBookerIdAndStartAfterOrderByStartDesc_test() {
        Booking booking = new Booking(1L, LocalDateTime.now().plusDays(30), LocalDateTime.now().plusDays(15), item1, user, BookingStatus.REJECTED);
        Booking booking2 = new Booking(2L, LocalDateTime.now().minusDays(15), LocalDateTime.now().minusDays(11), item1, user, BookingStatus.REJECTED);
        Booking booking3 = new Booking(3L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(15), item1, user, BookingStatus.REJECTED);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);

        Assertions.assertEquals(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(1, LocalDateTime.now(),
                pageable), List.of(booking, booking3));
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDesc_test() {
        Booking booking = new Booking(1L, LocalDateTime.now().plusDays(30), LocalDateTime.now().plusDays(15), item1, user, BookingStatus.REJECTED);
        Booking booking2 = new Booking(2L, LocalDateTime.now().minusDays(15), LocalDateTime.now().minusDays(11), item1, user, BookingStatus.APPROVED);
        Booking booking3 = new Booking(3L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(15), item1, user, BookingStatus.REJECTED);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);

        Assertions.assertEquals(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(1, BookingStatus.REJECTED,
                pageable), List.of(booking, booking3));
    }

    @Test
    void findAllByItemOwnerIdOrderByStartDescTest() {
        Booking booking = new Booking(1L, LocalDateTime.MIN, LocalDateTime.MAX, item1, user, BookingStatus.REJECTED);
        Booking booking2 = new Booking(2L, LocalDateTime.MIN, LocalDateTime.MAX, item1, user2, BookingStatus.REJECTED);
        Booking booking3 = new Booking(3L, LocalDateTime.MIN, LocalDateTime.MAX, item2, user, BookingStatus.REJECTED);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);

        Assertions.assertEquals(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(1, pageable), List.of(booking, booking2));
    }

    @Test
    void findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDescTest() {
        Booking booking = new Booking(1L, LocalDateTime.now().plusDays(11), LocalDateTime.now().plusDays(15), item1, user, BookingStatus.REJECTED);
        Booking booking2 = new Booking(2L, LocalDateTime.now().minusDays(15), LocalDateTime.now().plusDays(11), item1, user, BookingStatus.REJECTED);
        Booking booking3 = new Booking(3L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(15), item1, user, BookingStatus.REJECTED);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);

        Assertions.assertEquals(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(1,
                LocalDateTime.now(), LocalDateTime.now(), pageable), List.of(booking2));
    }

    @Test
    void findAllByItemOwnerIdAndEndBeforeOrderByStartDescDescTest() {
        Booking booking = new Booking(1L, LocalDateTime.now().plusDays(11), LocalDateTime.now().minusDays(1), item1, user, BookingStatus.REJECTED);
        Booking booking2 = new Booking(2L, LocalDateTime.now().minusDays(15), LocalDateTime.now().minusDays(11), item1, user, BookingStatus.REJECTED);
        Booking booking3 = new Booking(3L, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(15), item1, user, BookingStatus.REJECTED);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);

        Assertions.assertEquals(bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(1,
                LocalDateTime.now(), pageable), List.of(booking, booking2));
    }

    @Test
    void findAllByItemOwnerIdAndStartAfterOrderByStartDescTest() {
        Booking booking = new Booking(1L, LocalDateTime.now().plusDays(11), LocalDateTime.now().plusDays(15), item1, user, BookingStatus.REJECTED);
        Booking booking2 = new Booking(2L, LocalDateTime.now().minusDays(15), LocalDateTime.now().minusDays(11), item1, user, BookingStatus.REJECTED);
        Booking booking3 = new Booking(3L, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(15), item1, user, BookingStatus.REJECTED);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);

        Assertions.assertEquals(bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(1,
                LocalDateTime.now(), pageable), List.of(booking));
    }

    @Test
    void findAllByItemOwnerIdAndStatusOrderByStartDescTest() {
        Booking booking = new Booking(1L, LocalDateTime.now().plusDays(11), LocalDateTime.now().plusDays(15), item1, user, BookingStatus.REJECTED);
        Booking booking2 = new Booking(2L, LocalDateTime.now().minusDays(15), LocalDateTime.now().minusDays(11), item1, user, BookingStatus.WAITING);
        Booking booking3 = new Booking(3L, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(15), item1, user, BookingStatus.REJECTED);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);

        Assertions.assertEquals(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(1,
                BookingStatus.REJECTED, pageable), List.of(booking, booking3));
    }

    @Test
    void findAllByItemIdAndStartBeforeAndStatusOrderByStartDescTest() {
        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(11), LocalDateTime.now().plusDays(15), item1, user, BookingStatus.REJECTED);
        Booking booking2 = new Booking(2L, LocalDateTime.now().minusDays(15), LocalDateTime.now().minusDays(11), item1, user, BookingStatus.WAITING);
        Booking booking3 = new Booking(2L, LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(11), item1, user, BookingStatus.WAITING);
        Booking booking4 = new Booking(3L, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(15), item2, user, BookingStatus.REJECTED);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);

        Assertions.assertEquals(bookingRepository.findAllByItemIdAndStartBeforeAndStatusOrderByStartDesc(1,
                LocalDateTime.now(), BookingStatus.REJECTED), List.of(booking));
    }

    @Test
    void findAllByItemIdAndStartAfterAndStatusOrderByStartAscTest() {
        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(11), LocalDateTime.now().plusDays(15), item1, user, BookingStatus.REJECTED);
        Booking booking2 = new Booking(2L, LocalDateTime.now().minusDays(15), LocalDateTime.now().minusDays(11), item1, user, BookingStatus.WAITING);
        Booking booking3 = new Booking(2L, LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(11), item1, user, BookingStatus.REJECTED);
        Booking booking4 = new Booking(3L, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(15), item2, user, BookingStatus.REJECTED);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);

        Assertions.assertEquals(bookingRepository.findAllByItemIdAndStartAfterAndStatusOrderByStartAsc(1,
                LocalDateTime.now(), BookingStatus.REJECTED), List.of(booking3));
    }

    @Test
    void findBookingByItemIdAndBookerIdAndEndBeforeAndStatusTest() {
        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(12), LocalDateTime.now().minusDays(1), item1, user, BookingStatus.REJECTED);
        Booking booking2 = new Booking(2L, LocalDateTime.now().minusDays(12), LocalDateTime.now().minusDays(1), item1, user, BookingStatus.WAITING);
        Booking booking3 = new Booking(3L, LocalDateTime.now().minusDays(12), LocalDateTime.now().plusDays(11), item1, user, BookingStatus.REJECTED);
        Booking booking4 = new Booking(4L, LocalDateTime.now().plusDays(12), LocalDateTime.now().minusDays(1), item1, user2, BookingStatus.REJECTED);
        Booking booking5 = new Booking(5L, LocalDateTime.now().minusDays(12), LocalDateTime.now().minusDays(1), item2, user, BookingStatus.REJECTED);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        bookingRepository.save(booking5);

        Assertions.assertEquals(bookingRepository.findBookingByItemIdAndBookerIdAndEndBeforeAndStatus(1, 1,
                LocalDateTime.now(), BookingStatus.REJECTED), List.of(booking));
    }
}