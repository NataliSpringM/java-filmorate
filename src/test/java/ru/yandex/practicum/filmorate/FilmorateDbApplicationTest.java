package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmorateDbApplicationTest {

    private final UserStorage userStorage;
    private final UserService userService;
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final RatingMpaStorage mpaStorage;
    private final FilmGenreStorage genreStorage;
    User userAlex1;
    User userEgor2;
    User userAnna3;
    User userOlga4;
    Film filmAllHatesCris;
    Film filmTomAndJerry;
    Film filmDiamondHand;
    Mpa mpa;
    Set<FilmGenre> genres;


    @BeforeEach
    public void create() {

        userAlex1 = User.builder()
                .email("Alex@yandex.ru")
                .login("alex")
                .name("Alexandr Ivanov")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userEgor2 = User.builder()
                .email("Egor@yandex.ru")
                .login("egor")
                .name(" ")
                .birthday(LocalDate.of(2000, 3, 3))
                .build();
        userAnna3 = User.builder()
                .email("Anna@yandex.ru")
                .login("anna")
                .name("Anna Smith")
                .birthday(LocalDate.of(2000, 12, 12))
                .build();
        userOlga4 = User.builder()
                .email("Olga@yandex.ru")
                .login("olga")
                .name("Olga Smith")
                .birthday(LocalDate.of(2002, 1, 1))
                .build();
        mpa = Mpa.builder().id(1).build();
        genres = Set.of(new FilmGenre(2, null), new FilmGenre(3, null));
        filmAllHatesCris = Film.builder()
                .name("All hates Cris")
                .description("Good comedy")
                .duration(30)
                .releaseDate(LocalDate.of(1998, 1, 1))
                .mpa(mpa)
                .genres(genres)
                .build();
        filmDiamondHand = Film.builder()
                .name("Diamond hand")
                .description("Good comedy")
                .duration(90)
                .releaseDate(LocalDate.of(1978, 1, 1))
                .mpa(Mpa.builder().id(2).build())
                .build();
        filmTomAndJerry = Film.builder()
                .name("Tom and Jerry")
                .description("Children cartoon")
                .duration(10)
                .releaseDate(LocalDate.of(1998, 1, 1))
                .mpa(Mpa.builder().id(3).build())
                .build();


    }

    //************************* Тестирование работы сервиса работы с пользователями *************************
    @Test
    public void shouldAddUserAndFindUserById() { // создание пользователя и его возврат по id

        User user1 = userStorage.addUser(userAlex1);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(user1.getId()));
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", user.getId())
                                .hasFieldOrPropertyWithValue("email", "Alex@yandex.ru")
                                .hasFieldOrPropertyWithValue("name", "Alexandr Ivanov")
                                .hasFieldOrPropertyWithValue("login", "alex")
                                .hasFieldOrPropertyWithValue("birthday",
                                        LocalDate.of(2000, 10, 10)));
    }


    @Test
    public void shouldAddUserWithEmptyNameAndFindUserById() { // создание пользователя с пустым именем

        User user1 = userStorage.addUser(userEgor2);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(user1.getId()));
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", user.getId())
                                .hasFieldOrPropertyWithValue("email", "Egor@yandex.ru")
                                .hasFieldOrPropertyWithValue("name", "egor")
                                .hasFieldOrPropertyWithValue("login", "egor")
                                .hasFieldOrPropertyWithValue("birthday",
                                        LocalDate.of(2000, 3, 3)));
    }

    @Test
    public void shouldUpdateUser() { // обновление пользователя

        User user1 = userStorage.addUser(userAlex1);
        User userAlex1Updated = User.builder()
                .id(user1.getId())
                .email("AlexSmith@google.ru")
                .login("alex")
                .name("Alex Smith")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        User user1Updated = userStorage.updateUser(userAlex1Updated);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(user1Updated.getId()));
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", user.getId())
                                .hasFieldOrPropertyWithValue("email", "AlexSmith@google.ru")
                                .hasFieldOrPropertyWithValue("name", "Alex Smith")
                                .hasFieldOrPropertyWithValue("login", "alex")
                                .hasFieldOrPropertyWithValue("birthday",
                                        LocalDate.of(2000, 10, 10)));
    }

    @Test
    public void shouldUpdateWithEmptyNameUser() { // обновление пользователя с пустым именем

        User user1 = userStorage.addUser(userAlex1);
        User userWithEmptyNameUpdated = User.builder()
                .id(user1.getId())
                .email("AlexSmith@google.ru")
                .login("alex")
                .name("")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        User user1Updated = userStorage.updateUser(userWithEmptyNameUpdated);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(user1Updated.getId()));
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", user.getId())
                                .hasFieldOrPropertyWithValue("email", "AlexSmith@google.ru")
                                .hasFieldOrPropertyWithValue("name", "alex")
                                .hasFieldOrPropertyWithValue("login", "alex")
                                .hasFieldOrPropertyWithValue("birthday",
                                        LocalDate.of(2000, 10, 10)));
    }

    @Test
    public void shouldListUsers() { // получение списка пользователей

        User user1 = userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);
        User user3 = userStorage.addUser(userAnna3);

        List<User> listUsers = userStorage.listUsers();

        assertThat(listUsers).asList().hasSize(3);

        assertThat(listUsers).asList().contains(userStorage.getUserById(user1.getId()));
        assertThat(listUsers).asList().contains(userStorage.getUserById(user2.getId()));
        assertThat(listUsers).asList().contains(userStorage.getUserById(user3.getId()));

        assertThat(Optional.of(listUsers.get(0)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("login", "alex"));

        assertThat(Optional.of(listUsers.get(1)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("login", "egor"));

        assertThat(Optional.of(listUsers.get(2)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("login", "anna"));
    }

    @Test
    public void shouldGetEmptyListUsers() { // получение пустого списка пользователей

        List<User> listUsers = userStorage.listUsers();

        assertThat(listUsers).asList().hasSize(0);
        assertThat(listUsers).asList().isEmpty();

    }


    @Test
    public void shouldAddFriend() { // добавление друга

        User user1 = userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);

        userService.addFriend(user1.getId(), user2.getId());

        List<User> listFriends = userService.listUserFriends(user1.getId());

        assertThat(listFriends).asList().hasSize(1);
        assertThat(listFriends).asList().contains(userStorage.getUserById(user2.getId()));

        assertThat(Optional.of(listFriends.get(0)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("email", "Egor@yandex.ru"));


    }

    @Test
    public void shouldDeleteFriend() { // удаление друга

        User user1 = userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);

        userService.addFriend(user1.getId(), user2.getId());
        userService.deleteFriend(user1.getId(), user2.getId());

        List<User> listFriends = userService.listUserFriends(user1.getId());

        assertThat(listFriends).asList().hasSize(0);
        assertThat(listFriends).asList().doesNotContain(userAlex1);

    }

    @Test
    public void shouldGetCommonFriends1() { // получение списка общих друзей (один друг)

        User user1 = userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);
        User user3 = userStorage.addUser(userAnna3);


        userService.addFriend(user1.getId(), user3.getId());
        userService.addFriend(user2.getId(), user3.getId());
        List<User> commonFriends = userService.listCommonFriends(user1.getId(), user2.getId());

        assertThat(commonFriends).asList().hasSize(1);
        assertThat(commonFriends).asList().contains(userStorage.getUserById(user3.getId()));

        assertThat(Optional.of(commonFriends.get(0)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("email", "Anna@yandex.ru"));


    }

    @Test
    public void shouldGetCommonFriends2() { // получение списка общих друзей (два друга)

        User user1 = userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);
        User user3 = userStorage.addUser(userAnna3);
        User user4 = userStorage.addUser(userOlga4);

        userService.addFriend(user1.getId(), user3.getId());
        userService.addFriend(user2.getId(), user3.getId());
        userService.addFriend(user1.getId(), user4.getId());
        userService.addFriend(user2.getId(), user4.getId());

        List<User> commonFriends = userService.listCommonFriends(user1.getId(), user2.getId());

        assertThat(commonFriends).asList().hasSize(2);

        assertThat(commonFriends).asList().contains(userStorage.getUserById(user3.getId()));
        assertThat(commonFriends).asList().contains(userStorage.getUserById(user4.getId()));

        assertThat(Optional.of(commonFriends.get(0)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("email", "Anna@yandex.ru"));

        assertThat(Optional.of(commonFriends.get(0)))
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user)
                                .hasFieldOrPropertyWithValue("email", "Anna@yandex.ru"));


    }

    @Test
    public void shouldGetEmptyCommonFriendList() { // получение пустого списка общих друзей

        User user1 = userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);
        User user3 = userStorage.addUser(userAnna3);

        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user1.getId(), user3.getId());

        List<User> commonFriends = userService.listCommonFriends(user1.getId(), user3.getId());

        assertThat(commonFriends).asList().hasSize(0);
        assertThat(commonFriends).asList().isEmpty();

    }

    @Test
    public void shouldConfirmMutualFriendship() { // подтверждение наличия двусторонней дружбы

        User user1 = userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);

        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user2.getId(), user1.getId());

        Boolean confirmation = userService.isFriendShipConfirmed(user1.getId(), user2.getId());
        Boolean confirmationReverse = userService.isFriendShipConfirmed(user1.getId(), user2.getId());

        assertThat(confirmation).isTrue();
        assertThat(confirmationReverse).isTrue();

    }

    @Test
    public void shouldFailConfirmMutualFriendship() { // подтверждение отсутствия двусторонней дружбы

        User user1 = userStorage.addUser(userAlex1);
        User user3 = userStorage.addUser(userEgor2);

        userService.addFriend(user1.getId(), user3.getId());

        Boolean confirmation = userService.isFriendShipConfirmed(user1.getId(), user3.getId());
        Boolean confirmationReverse = userService.isFriendShipConfirmed(user1.getId(), user3.getId());

        assertThat(confirmation).isFalse();
        assertThat(confirmationReverse).isFalse();

    }

    //************************* Тестирование работы сервиса работы с фильмами *************************
    @Test
    public void shouldAddFilmAndFindFilmById() { // добавление фильма и его получение по id

        Film film = filmStorage.addFilm(filmAllHatesCris);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(film.getId()));
        assertThat(filmOptional)
                .hasValueSatisfying(f ->
                        assertThat(f)
                                .hasFieldOrPropertyWithValue("id", film.getId())
                                .hasFieldOrPropertyWithValue("name", "All hates Cris")
                                .hasFieldOrPropertyWithValue("description", "Good comedy")
                                .hasFieldOrPropertyWithValue("releaseDate",
                                        LocalDate.of(1998, 1, 1))
                                .hasFieldOrPropertyWithValue("duration",
                                        30)
                                .hasFieldOrPropertyWithValue("mpa.id", 1));

    }

    @Test
    public void shouldUpdateFilm() { // обновление информации о фильме

        Film film = filmStorage.addFilm(filmAllHatesCris);
        Film updatedFilm = film.toBuilder().name("UPDATED").build();
        filmStorage.updateFilm(updatedFilm);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(film.getId()));
        assertThat(filmOptional)
                .hasValueSatisfying(f ->
                        assertThat(f)
                                .hasFieldOrPropertyWithValue("id", film.getId())
                                .hasFieldOrPropertyWithValue("name", "UPDATED")
                                .hasFieldOrPropertyWithValue("description", "Good comedy")
                                .hasFieldOrPropertyWithValue("releaseDate",
                                        LocalDate.of(1998, 1, 1))
                                .hasFieldOrPropertyWithValue("duration",
                                        30)
                                .hasFieldOrPropertyWithValue("mpa.id", 1)
                                .hasFieldOrPropertyWithValue("genres", genres));
    }

    @Test
    public void shouldUpdate2Film() { // обновление информации о фильме с изменением списка жанров и рейтинга

        Film film = filmStorage.addFilm(filmAllHatesCris);

        // обновляем жанры и Mpa
        Set<FilmGenre> genresNew = Set.of(new FilmGenre(2, null));
        Mpa mpaNew = new Mpa(4, null);

        Film updatedFilm = film.toBuilder().name("UPDATED").genres(genresNew).mpa(mpaNew).build();
        filmStorage.updateFilm(updatedFilm);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(film.getId()));
        assertThat(filmOptional)
                .hasValueSatisfying(f ->
                        assertThat(f)
                                .hasFieldOrPropertyWithValue("id", film.getId())
                                .hasFieldOrPropertyWithValue("name", "UPDATED")
                                .hasFieldOrPropertyWithValue("description", "Good comedy")
                                .hasFieldOrPropertyWithValue("releaseDate",
                                        LocalDate.of(1998, 1, 1))
                                .hasFieldOrPropertyWithValue("duration",
                                        30)
                                .hasFieldOrPropertyWithValue("mpa.id", 4)
                                .hasFieldOrPropertyWithValue("mpa.name", "R")
                                .hasFieldOrPropertyWithValue("genres", genresNew));
    }

    @Test
    public void shouldListFilms() { // получение списка фильмов

        Film film1 = filmStorage.addFilm(filmAllHatesCris);
        Film film2 = filmStorage.addFilm(filmDiamondHand);
        Film film3 = filmStorage.addFilm(filmTomAndJerry);


        List<Film> listFilms = filmStorage.listFilms();

        assertThat(listFilms).asList().hasSize(3);

        assertThat(listFilms).asList().contains(filmStorage.getFilmById(film1.getId()));
        assertThat(listFilms).asList().contains(filmStorage.getFilmById(film2.getId()));
        assertThat(listFilms).asList().contains(filmStorage.getFilmById(film3.getId()));

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "All hates Cris"));

        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Diamond hand"));

        assertThat(Optional.of(listFilms.get(2)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Tom and Jerry"));

    }

    @Test
    public void shouldListFilmsEmpty() { // получение пустого списка фильмов

        List<Film> listFilms = filmStorage.listFilms();

        assertThat(listFilms).asList().hasSize(0);
        assertThat(listFilms).asList().isEmpty();

    }


    @Test
    public void shouldAddLike() { // добавление лайка
        User user1 = userStorage.addUser(userAlex1);
        Film film1 = filmStorage.addFilm(filmAllHatesCris);

        likeStorage.addLike(film1.getId(), user1.getId());
        Long likes = likeStorage.getFilmLikesTotalCount(film1.getId());

        assertThat(likes).isEqualTo(1);

    }

    @Test
    public void shouldDeleteLike() { // удаление лайков
        User user1 = userStorage.addUser(userAlex1);
        Film film1 = filmStorage.addFilm(filmAllHatesCris);

        likeStorage.addLike(film1.getId(), user1.getId());
        likeStorage.deleteLike(film1.getId(), user1.getId());

        Long likes = likeStorage.getFilmLikesTotalCount(film1.getId());

        assertThat(likes).isEqualTo(0);

    }

    @Test
    public void shouldListMostPopularFilms() { // получение списка наиболее популярных фильмов

        // создаем трех пользователей и три фильма
        User user1 = userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);
        User user3 = userStorage.addUser(userAnna3);

        Film film1 = filmStorage.addFilm(filmAllHatesCris);
        Film film2 = filmStorage.addFilm(filmDiamondHand);
        Film film3 = filmStorage.addFilm(filmTomAndJerry);

        // ставим лайки фильмам 1 (3 штуки) и 2 (1 штука)
        likeStorage.addLike(film1.getId(), user1.getId());
        likeStorage.addLike(film1.getId(), user2.getId());
        likeStorage.addLike(film1.getId(), user3.getId());
        likeStorage.addLike(film2.getId(), user1.getId());

        // получаем список фильмов отсортированных по количеству лайков
        List<Film> listFilms = filmStorage.listMostPopularFilms(10);

        // проверяем корректность полученных данных - 3 фильма,

        assertThat(listFilms).asList().hasSize(3);

        // проверяем порядок фильмов в списке - на первом месте фильм с id 1, на последнем фильм без лайков

        assertThat(listFilms).asList().startsWith(filmStorage.getFilmById(film1.getId()));
        assertThat(listFilms).asList().contains(filmStorage.getFilmById(film2.getId()));
        assertThat(listFilms).asList().endsWith(filmStorage.getFilmById(film3.getId()));

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "All hates Cris"));

        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Diamond hand"));

        assertThat(Optional.of(listFilms.get(2)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Tom and Jerry"));
    }

    @Test
    public void shouldListMostPopularFilms2() { // получение списка наиболее популярных фильмов с ограничением размера

        // создаем трех пользователей и три фильма
        User user1 = userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);
        User user3 = userStorage.addUser(userAnna3);

        Film film1 = filmStorage.addFilm(filmAllHatesCris);
        Film film2 = filmStorage.addFilm(filmDiamondHand);
        Film film3 = filmStorage.addFilm(filmTomAndJerry);

        // ставим лайки фильмам 1 (3 штуки) и 2 (1 штука)
        likeStorage.addLike(film1.getId(), user1.getId());
        likeStorage.addLike(film1.getId(), user2.getId());
        likeStorage.addLike(film1.getId(), user3.getId());
        likeStorage.addLike(film2.getId(), user1.getId());

        // получаем список фильмов отсортированных по количеству лайков (ограничение размера - 2 фильма)
        List<Film> listFilms = filmStorage.listMostPopularFilms(2);

        // проверяем корректность полученных данных - 3 фильма,

        assertThat(listFilms).asList().hasSize(2);

        // проверяем порядок фильмов в списке - на первом месте фильм с id 1, на последнем фильм без лайков

        assertThat(listFilms).asList().startsWith(filmStorage.getFilmById(film1.getId()));
        assertThat(listFilms).asList().endsWith(filmStorage.getFilmById(film2.getId()));
        assertThat(listFilms).asList().doesNotContain(filmStorage.getFilmById(film3.getId()));

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "All hates Cris"));

        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Diamond hand"));


    }


    //************************* Тестирование работы сервиса работы с рейтингами MPA *************************

    @Test
    public void shouldListMpa() { // получение списка всех рейтингов, отсортированных по возрастанию id

        // получаем список возможных рейтингов
        List<Mpa> listMpa = mpaStorage.listRatingMpa();

        // проверяем корректность полученных данных - 5 вариантов

        assertThat(listMpa).asList().hasSize(5);

        // проверяем порядок рейтингов в списке - на первом месте фильм с id 1, на последнем фильм с id 5

        final Integer firstId = 1;
        final Integer lastId = 5;

        assertThat(listMpa).asList().startsWith(mpaStorage.getRatingMpaById(firstId));
        assertThat(listMpa).asList().endsWith(mpaStorage.getRatingMpaById(lastId));

    }

    @Test
    public void getMpaById() { // получение информации о рейтинге фильма по id

        final Integer secondId = 2;
        final String secondName = "PG";

        assertThat(mpaStorage.getRatingMpaById(secondId))
                .hasFieldOrPropertyWithValue("id", 2)
                .hasFieldOrPropertyWithValue("name", secondName);
    }

    //************************* Тестирование работы сервиса работы с жанрами фильмов *************************

    @Test
    public void shouldListGenres() { // получение списка всех жанров, отсортированных по возрастанию id

        // получаем список возможных рейтингов
        List<FilmGenre> listGenres = genreStorage.listFilmGenres();

        // проверяем корректность полученных данных - 6 вариантов

        assertThat(listGenres).asList().hasSize(6);

        // проверяем порядок рейтингов в списке - на первом месте фильм с id 1, на последнем фильм с id 6

        final Integer firstId = 1;
        final Integer lastId = 6;

        assertThat(listGenres).asList().startsWith(genreStorage.getGenreById(firstId));
        assertThat(listGenres).asList().endsWith(genreStorage.getGenreById(lastId));

    }

    @Test
    public void getGenreById() { // получение информации о жанре фильма по id

        final Integer id = 5;
        final String name5 = "Документальный";

        assertThat(genreStorage.getGenreById(id))
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("name", name5);
    }


}








