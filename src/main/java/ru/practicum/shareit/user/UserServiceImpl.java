package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public UserDto getById(long id) {
        return UserMapper.toUserDto(userRepository.getById(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto create(UserDto dto) {
        User newUser = UserMapper.toUser(dto);
        return UserMapper.toUserDto(userRepository.create(newUser));
    }

    @Override
    public UserDto update(UserDto dto, long id) {
        User newUser = UserMapper.toUser(dto);
        return UserMapper.toUserDto(userRepository.update(newUser, id));
    }

    @Override
    public void remove(long id) {
        userRepository.remove(id);
    }
}