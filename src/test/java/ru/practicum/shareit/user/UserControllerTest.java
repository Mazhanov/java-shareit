package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    UserService userService;

    @InjectMocks
    private UserController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void getAllTest() throws Exception {
        List<UserDto> users = new ArrayList<>();
        users.add(makeUserDtoWithId(1L));
        users.add(makeUserDtoWithId(2L));
        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(users.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(users.get(0).getName())))
                .andExpect(jsonPath("$[0].email", is(users.get(0).getEmail())));
    }

    @Test
    void getByIdTest() throws Exception {
        UserDto user = makeUserDtoWithId(1L);
        when(userService.getById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void createTest() throws Exception {
        UserDto userNotId = makeUserDto();
        UserDto user = makeUserDtoWithId(1L);
        when(userService.create(any())).thenReturn(user);

        mockMvc.perform(post("/users")
                .content(mapper.writeValueAsString(userNotId))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userNotId.getName())))
                .andExpect(jsonPath("$.email", is(userNotId.getEmail())));
    }

    @Test
    void createTest_WithIncorrectEmail() throws Exception {
        UserDto userNotId = makeUserDto();
        userNotId.setEmail("IncorrectEmail");

        mockMvc.perform(post("/users"))
                .andExpect(status().is(400));
    }

    @Test
    void updateTest() throws Exception {
        UserDto user = makeUserDtoWithId(1L);
        when(userService.update(any(), anyLong())).thenReturn(user);

        mockMvc.perform(patch("/users/{id}", user.getId())
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void updateTest_WithIncorrectEmail() throws Exception {
        UserDto userDto = makeUserDtoWithId(1L);
        userDto.setEmail("IncorrectEmail");

        mockMvc.perform(patch("/users/{id}", userDto.getId())
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void removeTest() throws Exception {

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());
    }

    private UserDto makeUserDto() {
        return new UserDto(null, "testName", "testEmail@email.ru");
    }

    private UserDto makeUserDtoWithId(long id) {
        return new UserDto(id, "testName", "testEmail@email.ru");
    }
}
