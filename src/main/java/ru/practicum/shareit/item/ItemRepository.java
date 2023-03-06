package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerIdOrderByIdAsc(long ownerId);

    @Query("select it " +
    "from Item it " +
    "where upper(it.name) like upper(concat('%', ?1, '%')) " +
    "or upper(it.description) like upper(concat('%', ?1, '%')) " +
    "and it.available is true")
    List<Item> search(String text);
}
