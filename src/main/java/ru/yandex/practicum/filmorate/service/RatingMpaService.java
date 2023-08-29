package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

// сервис для работы с рейтингами MPA

public interface RatingMpaService {
    List<Mpa> listRatingMpa(); // получение списка рейтингов MPA

    Mpa getRatingMPAById(Integer id); // получение рейтинга MPA по id

}
