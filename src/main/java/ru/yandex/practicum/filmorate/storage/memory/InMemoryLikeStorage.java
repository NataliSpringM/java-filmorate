package ru.yandex.practicum.filmorate.storage.memory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryLikeStorage implements LikeStorage {

    // реализация хранения информации о лайках в памяти

    private final FilmStorage filmStorage;

    // информация о лайках - <id фильма, <cписок пользователей, поставивших лайки фильму>
    private final Map<Integer, Set<Long>> likes = new HashMap<>();

    // добавление лайка
    @Override
    public void addLike(Integer filmId, Long userId) {

        // получаем список пользователей, поставивших лайки фильму
        Set<Long> filmLikesByUsers = getFilmLikesByUsers(filmId);

        // добавляем лайк от пользователя в случае отсутствия лайков
        if (!filmLikesByUsers.contains(userId)) {
            // добавляем пользователя в список поставивших лайки
            filmLikesByUsers.add(userId);

            // обновляем список поставивших лайки для фильма
            likes.put(filmId, filmLikesByUsers);

        } else {
            throw new RuntimeException("Вы уже ставили лайк этому фильму");
        }

        // обновление данных о фильме в хранилище фильмов
        updateFilmLikes(filmId);

        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);

    }

    // удаление лайка
    @Override
    public void deleteLike(Integer filmId, Long userId) {

        // получаем список пользователей, поставивших лайки фильму
        Set<Long> filmLikesByUsers = getFilmLikesByUsers(filmId);

        if (filmLikesByUsers.contains(userId)) {

            // удаляем пользователя из списка поставивших лайки
            filmLikesByUsers.remove(userId);
            // обновляем список поставивших лайки для фильма
            likes.put(filmId, filmLikesByUsers);

        } else {

            throw new RuntimeException("Вы не ставили лайк этому фильму");
        }

        // обновление данных о фильме в хранилище
        updateFilmLikes(filmId);

        log.info("Пользователь {} удалил лайк у фильма {}",
                userId, filmId);

    }

    // подсчет лайков определенному фильму от всех пользователей
    @Override
    public Long getFilmLikesTotalCount(Integer filmId) {

        Set<Long> filmLikesByUsers = getFilmLikesByUsers(filmId);
        return (long) filmLikesByUsers.size(); // считаем количество лайков у фильма

    }

    // возвращение информации о лайках определенному фильму от пользователей
    private Set<Long> getFilmLikesByUsers(Integer filmId) {

        return likes.computeIfAbsent(filmId, set -> new HashSet<>());
    }

    // создание объекта фильма с обновленным количеством лайков и сохранение информации в хранилище фильмов
    private void updateFilmLikes(Integer filmId) {

        Film film = filmStorage.getFilmById(filmId).toBuilder()
                .likes(getFilmLikesTotalCount(filmId))
                .build();

        filmStorage.updateFilmData(film);

    }


}
