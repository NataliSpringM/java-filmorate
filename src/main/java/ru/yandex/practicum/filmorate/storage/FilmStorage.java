package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

// хранение информации о фильмах

public interface FilmStorage {

    Film addFilm(Film film);  //добавление информации о фильме

    Film updateFilm(Film film); // обновление информации о фильме

    List<Film> listFilms(); // получение списка фильмов

    Film getFilmById(Integer id); // получение фильма по идентификатору

    void updateFilmData(Film film); // сохранение новой или обновленной информации о фильме

    List<Film> listMostPopularFilms(Integer count); // получение списка наиболее популярных фильмов

    default List<Film> listFilmsOfDirector(Integer directorId) { // получение списка фильмов по режиссеру
        return null;
    }

    List<Film> listSearchResults(String substringQuery, List<String> searchBaseBy); // поиск по названию или режиссеру

    void checkFilmId(Integer filmId); // проверка существования id фильма

	boolean delete(Integer id); // удаление фильма по id

	void clearAll(); // удаление всех фильмов

    List<Film> getCommonFilmsBetweenUsers(Long userId, Long friendId);

}
