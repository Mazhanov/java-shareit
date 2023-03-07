package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
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
        return UserMapper.toUserDto(findUserByIdAndCheck(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto create(UserDto dto) {
        User newUser = UserMapper.toUser(dto);
        return UserMapper.toUserDto(userRepository.save(newUser));
    }

    @Override
    public UserDto update(UserDto dto, long id) {
        User userBase = findUserByIdAndCheck(id);

        if (dto.getName() != null && !dto.getName().equals(userBase.getName())) {
            userBase.setName(dto.getName());
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(userBase.getEmail())) {
            userBase.setEmail(dto.getEmail());
        }

        return UserMapper.toUserDto(userRepository.save(userBase));
    }

    @Override
    public void remove(long id) {
        User user = findUserByIdAndCheck(id);
        userRepository.delete(user);
    }

    private User findUserByIdAndCheck(long id) { // Возвращает юзера по ID и проверяет наличие в БД
        return userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь с id" + id + " не найден"));
    }
}