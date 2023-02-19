package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getById(long id) {
        return userRepository.getById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User create(UserCreateDto dto) {
        User newUser = UserMapper.toUserCreate(dto);
        return userRepository.create(newUser);
    }

    @Override
    public User update(UserUpdateDto dto, long id) {
        User newUser = UserMapper.toUserUpdate(dto, id);
        return userRepository.update(newUser);
    }

    @Override
    public void remove(long id) {
        userRepository.remove(id);
    }
}
