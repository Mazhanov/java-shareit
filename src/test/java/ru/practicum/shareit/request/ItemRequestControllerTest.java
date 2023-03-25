package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mock
    RequestService requestService;

    @InjectMocks
    private ItemRequestController requestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(requestController)
                .build();
    }

    @Test
    void createTest() throws Exception {
        ItemRequestDto itemRequestDto = makeItemRequestDto(1L);
        itemRequestDto.setId(null);
        ItemRequestDto newRequest = makeItemRequestDto(1L);

        when(requestService.create(any(), anyLong(), any())).thenReturn(newRequest);

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(newRequest.getId()), Long.class))
                .andExpect(content().json(mapper.writeValueAsString(newRequest)));
    }

    @Test
    void getAllOwnTest() throws Exception {
        List<ItemRequestDto> requestsDto = List.of(makeItemRequestDto(1L), makeItemRequestDto(2L));

        when(requestService.getAllOwn(anyLong())).thenReturn(requestsDto);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestsDto.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(requestsDto.get(1).getId()), Long.class))
                .andExpect(content().json(mapper.writeValueAsString(requestsDto)));
    }

    @Test
    void getAllRequestOtherUsersTest() throws Exception {
        List<ItemRequestDto> requestsDto = List.of(makeItemRequestDto(1L), makeItemRequestDto(2L));

        when(requestService.getAllRequestOtherUsers(anyLong(), any())).thenReturn(requestsDto);

        mockMvc.perform(get("/requests/all")
                        .param("from", "1")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestsDto.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(requestsDto.get(1).getId()), Long.class))
                .andExpect(content().json(mapper.writeValueAsString(requestsDto)));
    }

    @Test
    void getByIdTest() throws Exception {
        ItemRequestDto requestDto = makeItemRequestDto(1L);

        when(requestService.getById(anyLong(), anyLong())).thenReturn(requestDto);

        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(content().json(mapper.writeValueAsString(requestDto)));
    }

    private ItemRequestDto makeItemRequestDto(long id){
        return new ItemRequestDto(id, "description", LocalDateTime.now(), List.of());
    }


}