package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserRepository {
    private Map<Long, User> users = new HashMap<>();
    private Map<String, Long> emails = new HashMap<>();
    private long userId = 0;

    public User getById(long id) {
        checkId(id);
        return users.get(id);
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User create(User user) {
        checkEmail(user);
        generateUserId(user);
        emails.put(user.getEmail(), user.getId());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        checkId(user.getId());

        if (!(user.getEmail() == null)) {
            if (!(user.getEmail().equals(users.get(user.getId()).getEmail()))) {
                checkEmail(user);
                emails.remove(users.get(user.getId()).getEmail());
                emails.put(user.getEmail(), user.getId());
            }
        }

        if (user.getName() != null) {
            users.get(user.getId()).setName(user.getName());
        }
        if (user.getEmail() != null) {
            users.get(user.getId()).setEmail(user.getEmail());
        }
        return users.get(user.getId());
    }

    public void remove(long id) {
        checkId(id);
        emails.remove(users.get(id).getEmail());
        users.remove(id);
    }

    private void generateUserId(User user) {
        userId++;
        user.setId(userId);
    }

    private void checkId(long id) {
        if (!users.containsKey(id)) {
            log.warn("Пользователь с id {} не найден", id);
            throw new ObjectNotFoundException("Пользователь с id" + id + " не найден");
        }
    }

    private void checkEmail(User user) {
        if (emails.containsKey(user.getEmail())) {
            log.warn("Email {} Уже занят", user.getEmail());
            throw new EmailDuplicateException("Email " + user.getEmail() + " уже занят");
        }
    }
}
