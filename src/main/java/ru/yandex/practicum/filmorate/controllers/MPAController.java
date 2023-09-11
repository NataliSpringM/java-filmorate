package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.RatingMpaService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
@Validated
@RequiredArgsConstructor
public class MPAController {

      /* обработка запросов HTTP-клиентов на  получение информации о рейтингах фильмов по адресу
    http://localhost:8080/mpa */

    private final RatingMpaService mpaService;

    // обработка GET-запроса на получение списка всех рейтингов
    @GetMapping()
    public List<Mpa> listRatingMPA() {

        return mpaService.listRatingMpa();
    }

    // обработка GET-запроса на получение рейтинга по id
    @GetMapping("/{id}")
    public Mpa getGenreById(@PathVariable Integer id) {

        return mpaService.getRatingMPAById(id);
    }

}



