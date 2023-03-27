package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

@Transactional
@DataJpaTest
@AutoConfigureTestDatabase
class ItemRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    void searchTest() {
        EntityManager entityManager = em.getEntityManager();
        TypedQuery<Item> query = entityManager
                .createQuery("select it " +
                        "from Item it " +
                        "where upper(it.name) like upper(concat('%', ?1, '%')) " +
                        "or upper(it.description) like upper(concat('%', ?1, '%')) " +
                        "and it.available is true", Item.class);
        User user = new User(1L, "name", "email@email.ru");
        userRepository.save(user);

        Item item1 = makeItem(1L, "отвертка", "описание", user);
        Item item2 = makeItem(2L, "супер отвертка", "описание", user);
        Item item3 = makeItem(3L, "дрель", "описание", user);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        Assertions.assertEquals(query.setParameter(1, "отвертка").getResultList(), List.of(item1, item2));
    }

    private Item makeItem(long id, String name, String description, User user) {
        return new Item(id, name, description, false, user, null);
    }
}