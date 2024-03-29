package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.PageableCreate;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;

import java.util.List;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceImplIntegrationTest {
    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;

    @Test
    void createTest() {
        UserDto user = new UserDto(1L, "testName", "email@mail.ru");
        UserDto newUser = userService.create(user);

        ItemDto itemDto = makeItemDto("item1");
        ItemDto itemDto2 = makeItemDto("item2");
        ItemDto itemDto3 = makeItemDto("item3");
        itemService.create(itemDto, 1L);
        itemService.create(itemDto2, 1L);
        itemService.create(itemDto3, 1L);

        List<ItemDto> items = itemService.getByUserId(newUser.getId(), PageableCreate.pageableCreate(1, 5));
        Assertions.assertEquals(items.size(), 3);
    }

    @Test
    void searchTest() {
        UserDto user = new UserDto(1L, "testName", "email@mail.ru");
        UserDto newUser = userService.create(user);

        ItemDto itemDto = makeItemDto("отвертка");
        ItemDto itemDto2 = makeItemDto("супер отвертка");
        ItemDto itemDto3 = makeItemDto("дрель");
        itemService.create(itemDto, 1L);
        itemService.create(itemDto2, 1L);
        itemService.create(itemDto3, 1L);

        List<ItemDto> items = itemService.search("отвертка", PageableCreate.pageableCreate(1, 10));
        Assertions.assertEquals(items.size(), 2);
        Assertions.assertEquals(items.get(0).getName(), itemDto.getName());
        Assertions.assertEquals(items.get(1).getName(), itemDto2.getName());
    }

    private ItemDto makeItemDto(String name) {
        return new ItemDto(null, name, "description", false, null);
    }
}