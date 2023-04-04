package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mock
    ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .build();
    }

    @Test
    void createTest() throws Exception {
        ItemDto itemDto = makeItemDtoWithId(1L);
        ItemDto newItemDto = makeItemDto();
        when(itemService.create(any(), anyLong())).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(newItemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner", is(itemDto.getOwner())));
    }

    /*@Test
    void createTest_BlankName() throws Exception {
        ItemDto newItemDto = makeItemDto();
        newItemDto.setName("");

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(newItemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }*/

    /*@Test
    void createTest_BlankDescription() throws Exception {
        ItemDto newItemDto = makeItemDto();
        newItemDto.setDescription("");

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(newItemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }*/

    /*@Test
    void createTest_NullAvailable() throws Exception {
        ItemDto newItemDto = makeItemDto();
        newItemDto.setAvailable(null);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(newItemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }*/

    @Test
    void updateTest() throws Exception {
        ItemDto itemDto = makeItemDtoWithId(1L);
        ItemDto newItemDto = makeItemDtoWithId(1L);

        when(itemService.update(any(), anyLong(), anyLong())).thenReturn(itemDto);

        mockMvc.perform(patch("/items/{id}", itemDto.getId())
                        .content(mapper.writeValueAsString(newItemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner", is(itemDto.getOwner())));
    }

    @Test
    void getByUserIdTest() throws Exception {
        List<ItemDto> items = List.of(makeItemDtoWithId(1L), makeItemDtoWithId(2L));
        when(itemService.getByUserId(anyLong(),any())).thenReturn(items);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(items.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(items.get(1).getId()), Long.class));
    }

    @Test
    void getByIdTest() throws Exception {
        ItemDto itemDto = makeItemDtoWithId(1L);
        when(itemService.getById(anyLong(),anyLong())).thenReturn(itemDto);

        mockMvc.perform(get("/items/{id}", itemDto.getId())
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner", is(itemDto.getOwner())));
    }

    @Test
    void searchTest() throws Exception {
        List<ItemDto> items = List.of(makeItemDtoWithId(1L));
        when(itemService.search(anyString(),any())).thenReturn(items);

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .queryParam("text", "textSearch"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(items.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(items.get(0).getName())))
                .andExpect(jsonPath("$[0].description", is(items.get(0).getDescription())))
                .andExpect(jsonPath("$[0].available", is(items.get(0).getAvailable())))
                .andExpect(jsonPath("$[0].owner", is(items.get(0).getOwner())));
    }

    @Test
    void createCommentTest() throws Exception {
        LocalDateTime date = LocalDateTime.now();
        CommentDto newCommentDto = new CommentDto(null, "text", "authorName", date);
        CommentDto commentDto = new CommentDto(1L, "text", "authorName", date);
        String json = mapper.writeValueAsString(newCommentDto);

        when(itemService.createComment(anyLong(), anyLong(), any(), any())).thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(json)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }

    private ItemDto makeItemDto() {
        return new ItemDto(null, "name", "description", false, null);
    }

    private ItemDto makeItemDtoWithId(long id) {
        return new ItemDto(id, "name", "description", false, null);
    }
}