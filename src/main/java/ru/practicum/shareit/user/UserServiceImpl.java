package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public User getById(long id) {
        return userRepository.getById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User create(UserDto dto) {
        User newUser = UserMapper.toUser(dto);
        return userRepository.create(newUser);
    }

    @Override
    public User update(UserDto dto, long id) {
        User newUser = UserMapper.toUser(dto);
        return userRepository.update(newUser, id);
    }

    @Override
    public void remove(long id) {
        userRepository.remove(id);
    }
}