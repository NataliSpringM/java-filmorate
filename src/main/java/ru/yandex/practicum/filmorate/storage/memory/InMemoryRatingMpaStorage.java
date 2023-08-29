package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryRatingMpaStorage implements RatingMpaStorage {
    // реализация хранения информации о рейтингах фильмов в памяти
    private final Map<Integer, Mpa> ratingMap = new HashMap<>();
    private static final Mpa MPA_1 = new Mpa(1, "G");
    private static final Mpa MPA_2 = new Mpa(2, "PG");
    private static final Mpa MPA_3 = new Mpa(3, "PG-13");
    private static final Mpa MPA_4 = new Mpa(4, "R");
    private static final Mpa MPA_5 = new Mpa(5, "NC-17");


    private InMemoryRatingMpaStorage() {

        ratingMap.put(1, MPA_1);
        ratingMap.put(2, MPA_2);
        ratingMap.put(3, MPA_3);
        ratingMap.put(4, MPA_4);
        ratingMap.put(5, MPA_5);

    }

    // получение списка возможных рейтингов
    @Override
    public List<Mpa> listRatingMpa() {

        return new ArrayList<>(ratingMap.values());
    }

    // получение рейтинга по id
    @Override
    public Mpa getRatingMpaById(Integer id) {

        if (ratingMap.containsKey(id)) {

            return ratingMap.get(id);
        } else {
            throw new ObjectNotFoundException(String.format("Рейтинг с id: %d не найден", id));
        }
    }
}
