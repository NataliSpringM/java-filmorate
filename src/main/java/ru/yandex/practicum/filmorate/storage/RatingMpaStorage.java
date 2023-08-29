package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface RatingMpaStorage {

    // хранение информации о рейтингах фильмов
    List<Mpa> listRatingMpa(); // получение списка рейтингов в системе MPA

    Mpa getRatingMpaById(Integer id); // получение значение рейтинга MPA по id
}
