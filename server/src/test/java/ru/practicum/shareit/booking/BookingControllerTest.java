package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mock
    BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .build();
    }

    @Test
    void createTest() throws Exception {
        CreateBookingDto createBookingDto = makeCreateBookingDto();
        BookingDto bookingDto = makeBookingDto(1L);
        Item item = makeItem(1L);
        User user = makeUser(1L);
        bookingDto.setItem(item);
        bookingDto.setBooker(user);
        when(bookingService.createBooking(any(), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(createBookingDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void changeStatusBookingTest() throws Exception {
        BookingDto bookingDto = makeBookingDto(1L);
        Item item = makeItem(1L);
        User user = makeUser(1L);
        bookingDto.setItem(item);
        bookingDto.setBooker(user);
        bookingDto.setStatus(BookingStatus.APPROVED);
        when(bookingService.changeStatusBooking(anyLong(),anyLong(), anyBoolean())).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void getBookingTest() throws Exception {
        BookingDto bookingDto = makeBookingDto(1L);
        Item item = makeItem(1L);
        User user = makeUser(1L);
        bookingDto.setItem(item);
        bookingDto.setBooker(user);
        when(bookingService.getBooking(anyLong(),anyLong())).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void getAllByBookerTest() throws Exception {
        List<BookingDto> bookings = List.of(makeBookingDto(1L), makeBookingDto(2L));

        when(bookingService.getAllByBooker(anyLong(),any(), any())).thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "1")
                        .param("size", "20")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookings.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(bookings.get(1).getId()), Long.class))
                .andExpect(content().json(mapper.writeValueAsString(bookings)));
    }

    @Test
    void getAllByOwnerTest() throws Exception {
        List<BookingDto> bookings = List.of(makeBookingDto(1L), makeBookingDto(2L), makeBookingDto(3L));

        when(bookingService.getAllByOwner(anyLong(),any(), any())).thenReturn(bookings);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "CURRENT")
                        .param("from", "1")
                        .param("size", "20")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookings.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(bookings.get(1).getId()), Long.class))
                .andExpect(jsonPath("$[2].id", is(bookings.get(2).getId()), Long.class))
                .andExpect(content().json(mapper.writeValueAsString(bookings)));
    }

    private CreateBookingDto makeCreateBookingDto() {
        return new CreateBookingDto(null, LocalDateTime.MIN, LocalDateTime.MAX,
                1L, 1L, BookingStatus.WAITING);
    }

    private BookingDto makeBookingDto(long id) {
        return new BookingDto(id, LocalDateTime.MIN, LocalDateTime.MAX,
                null, null, BookingStatus.WAITING);
    }

    private Item makeItem(long id) {
        return new Item(id, "name", "description", false, null, null);
    }

    private User makeUser(long id) {
        return new User(id, "name", "email@email.ru");
    }
}