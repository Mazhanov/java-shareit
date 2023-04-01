package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getByIdTest() {
        User user = makeUser(1);
        UserDto userDto = makeUserDto(1);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        Assertions.assertEquals(userService.getById(1), userDto);
    }

    @Test
    void getByIdTest_returnUnavailableUser() {
        User user = makeUser(1);
        UserDto userDto = makeUserDto(1);
        when(userRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException("Пользователь с id1 не найден"));

        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> userService.getById(1));

        Assertions.assertEquals("Пользователь с id1 не найден", exception.getMessage());
    }

    @Test
    void getAllTest() {
        List<User> users = List.of(makeUser(1), makeUser(2), makeUser(3));
        List<UserDto> usersDto = List.of(makeUserDto(1), makeUserDto(2), makeUserDto(3));
        when(userRepository.findAll())
                .thenReturn(users);

        Assertions.assertEquals(userService.getAll(), usersDto);
    }

    @Test
    void getAllTest_emptyList() {
        List<User> users = new ArrayList<>();
        List<UserDto> usersDto = new ArrayList<>();
        when(userRepository.findAll())
                .thenReturn(users);

        Assertions.assertEquals(userService.getAll(), usersDto);
    }

    @Test
    void updateTest() {
        UserDto userDto = new UserDto(1L, "updateName", "updateEmail");
        User user = makeUser(1);
        when(userRepository.save(any()))
                .thenReturn(user);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        Assertions.assertEquals(userService.update(userDto, 1), userDto);
    }

    @Test
    void updateEmailTest() {
        UserDto userDto = new UserDto(null, null, "updateEmail");
        User user = makeUser(1);
        User updateUser = new User(1L, "testName", "updateEmail");
        when(userRepository.save(any()))
                .thenReturn(updateUser);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        Assertions.assertEquals(userService.update(userDto, 1), UserMapper.toUserDto(updateUser));
    }

    @Test
    void updateNameTest() {
        UserDto userDto = new UserDto(null, "updateName", null);
        User user = makeUser(1);
        User updateUser = new User(1L, "updateName", "testEmail");
        when(userRepository.save(any()))
                .thenReturn(updateUser);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        Assertions.assertEquals(userService.update(userDto, 1), UserMapper.toUserDto(updateUser));
    }

    @Test
    void removeTest() {
        User user = makeUser(1);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        userService.remove(1L);
        Mockito.verify(userRepository, Mockito.times(1))
                .delete(any());
    }

    private UserDto makeUserDto(long userId) {
        return new UserDto(userId, "testName", "testEmail");
    }

    private User makeUser(long userId) {
        return new User(userId, "testName", "testEmail");
    }
}