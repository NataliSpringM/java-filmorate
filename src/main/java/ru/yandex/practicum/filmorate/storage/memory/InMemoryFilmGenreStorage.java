package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@Component
public class InMemoryFilmGenreStorage implements FilmGenreStorage {

    // реализация хранения информации о жанрах фильмов в памяти
    private final HashMap<Integer, FilmGenre> genresMap = new HashMap<>();

    private static final FilmGenre FILM_GENRE_1 = new FilmGenre(1, "Комедия");
    private static final FilmGenre FILM_GENRE_2 = new FilmGenre(2, "Драма");
    private static final FilmGenre FILM_GENRE_3 = new FilmGenre(3, "Мультфильм");
    private static final FilmGenre FILM_GENRE_4 = new FilmGenre(4, "Триллер");
    private static final FilmGenre FILM_GENRE_5 = new FilmGenre(5, "Документальный");
    private static final FilmGenre FILM_GENRE_6 = new FilmGenre(6, "Боевик");


    private InMemoryFilmGenreStorage() {
        genresMap.put(1, FILM_GENRE_1);
        genresMap.put(2, FILM_GENRE_2);
        genresMap.put(3, FILM_GENRE_3);
        genresMap.put(4, FILM_GENRE_4);
        genresMap.put(5, FILM_GENRE_5);
        genresMap.put(6, FILM_GENRE_6);
    }

    // получение списка жанров
    @Override
    public List<FilmGenre> listFilmGenres() {
        return new ArrayList<>(genresMap.values());
    }

    @Override
    public FilmGenre getGenreById(Integer id) {

        if (genresMap.containsKey(id)) {
            return genresMap.get(id);
        } else {
            throw new ObjectNotFoundException(String.format("Жанр с id: %d не найден", id));
        }

    }
}
