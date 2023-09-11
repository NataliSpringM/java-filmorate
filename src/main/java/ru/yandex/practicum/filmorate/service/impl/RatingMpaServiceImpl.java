package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.RatingMpaService;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.util.List;

// реализация сервиса для получения информации о жанрах фильмов

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingMpaServiceImpl implements RatingMpaService {

    private final RatingMpaStorage ratingMpaStorage;

    // получение списка рейтингов
    @Override
    public List<Mpa> listRatingMpa() {

        return ratingMpaStorage.listRatingMpa();
    }

    // получение рейтинга по id
    @Override
    public Mpa getRatingMPAById(Integer id) {

        return ratingMpaStorage.getRatingMpaById(id);
    }
}
