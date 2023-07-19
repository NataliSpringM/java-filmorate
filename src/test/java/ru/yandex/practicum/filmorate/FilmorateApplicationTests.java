
package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@SpringBootTest
public class FilmorateApplicationTests {

    @Autowired
    private UserController userController;
    @Autowired
    private FilmController filmController;
    private Map<Integer, Film> films;
    private Map<Integer, User> users;
    private Validator validator;

    @BeforeEach
    void setUp() {

        users = userController.getUsersData();
        films = filmController.getFilmsData();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    //************************* Тестирование работы с информацией о пользователях *************************

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldAddUserWithValidInfo() {

        //добавляем пользователя с валидными данными

        User user = new User(1, "Alex@yandex.ru", "Alexandr Ivanov", "alex",
                LocalDate.of(1990, 10, 10));
        final Integer id = user.getId();
        userController.addUser(user);

        // проверяем наличие пользователя в сохраненных данных

        assertEquals(users.get(id), user, "Валидные данные пользователя не сохранились");

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldAddUserWithEmptyName() {

        //добавляем пользователя с пустым именем

        User user = new User(1, "Alex@yandex.ru", null, "alex",
                LocalDate.of(1990, 10, 10));
        final Integer id = user.getId();
        userController.addUser(user);

        // проверяем наличие пользователя в сохраненных данных и обновление имени значением логина

        assertEquals(users.get(id), user, "Данные пользователя c пустым именем не сохранились");
        assertEquals(users.get(id).getName(), users.get(id).getLogin(),
                "Имя не обновлено значением логина");

    }

    @ParameterizedTest
    @ArgumentsSource(UsersArgumentsProvider.class) // набор пользователей с навалидными данными
    public void shouldNotAddUser(User user) {

        //проверка выброшенного исключения при попытке сохранить пользователя с невалидными данными

        assertThrows(
                ConstraintViolationException.class,
                () -> userController.addUser(user),
                "Сохранены данные пользователя с невалидными данными.");

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldPutUserWithValidInfo() {

        //сохраняем пользователя

        User user = new User(1, "Alex@yandex.ru", "Alexandr Ivanov", "alex",
                LocalDate.of(1990, 10, 10));
        final Integer id = user.getId();
        userController.addUser(user);

        //обновляем данные пользователя

        User userUpdated = new User(1, "Egor@yandex.ru", "Egor Egorov", "egor",
                LocalDate.of(1990, 10, 10));
        userController.updateUser(userUpdated);

        //проверяем корректность обновления данных

        assertEquals(users.get(id), userUpdated, "Данные пользователя не обновились");

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldPutUserWithEmptyName() {

        //сохраняем пользователя с пустым именем

        User user = new User(1, "Alex@yandex.ru", null, "alex",
                LocalDate.of(1990, 10, 10));
        final Integer id = user.getId();
        userController.addUser(user);

        //обновляем данные пользователя

        User userUpdated = new User(1, "Egor@yandex.ru", null, "egor",
                LocalDate.of(1990, 10, 10));
        userController.updateUser(userUpdated);

        //проверяем корректность обновления данных и присвоения логина в качестве имени

        assertEquals(users.get(id), userUpdated, "Данные пользователя c пустым именем не обновились");
        assertEquals(users.get(id).getName(), users.get(id).getLogin(),
                "Имя пользователя не обновлено значением логина");

    }

    @Test
    public void shouldFailPutUserWithInvalidLogin() {

        // обновляем данные пользователя  с несуществующим логином, проверяем выброшенное исключение

        User user = new User(10000, "Egor@yandex.ru", "Egor Egorov", "egor",
                LocalDate.of(1990, 10, 10));

        assertThrows(
                UserDoesNotExistException.class,
                () -> userController.updateUser(user),
                "Такого пользователя нет в списке.");

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldGetUsersList() {

        // добавляем пользователей в список

        User user1 = new User(1, "Alex@yandex.ru", null, "alex",
                LocalDate.of(1990, 10, 10));
        userController.addUser(user1);

        User user2 = new User(1, "Egor@yandex.ru", null, "egor",
                LocalDate.of(1990, 10, 10));
        userController.addUser(user2);

        //получаем пользователей из списка

        List<User> usersList = userController.listUsers();
        assertEquals(usersList.size(), 2, "Список пользователей неверного размера");

    }

    //************************* Тестирование работы с информацией о Фильмах *************************
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldAddFilmWithValidInfo() {

        //добавляем фильм с валидными данными

        Film film = new Film(1, "All hate Cris", " Good comedy",
                LocalDate.of(2002, 2, 10), 40);
        final Integer id = film.getId();
        filmController.addFilm(film);

        // проверяем наличие пользователя в сохраненных данных

        assertEquals(films.get(id), film, "Валидные данные фильма не сохранились");

    }


    @ParameterizedTest
    @ArgumentsSource(FilmsArgumentsProvider.class) // набор фильмов с навалидными данными
    public void shouldNotAddFilmWithInvalidData(Film film) {

        //проверка выброшенного исключения при попытке сохранить пользователя с невалидными данными

        assertThrows(
                ConstraintViolationException.class,
                () -> filmController.addFilm(film),
                "Сохранены данные фильма с невалидными данными.");

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldPutFilmWithValidInfo() {

        //сохраняем данные о фильме

        Film film = new Film(1, "All hate Cris", " Good comedy",
                LocalDate.of(2002, 2, 10), 40);
        final Integer id = film.getId();
        filmController.addFilm(film);

        //обновляем данные фильма

        Film filmUpdated = new Film(1, "All like Tests", " Real comedy",
                LocalDate.of(2023, 7, 19), 12000);
        filmController.updateFilm(filmUpdated);

        //проверяем корректность обновления данных

        assertEquals(films.get(id), filmUpdated, "Данные фильма не обновились");

    }

    @Test
    public void shouldFailPutFilmWithInvalidLogin() {

        // обновляем данные фильма с несуществующим логином, проверяем выброшенное исключение

        Film film = new Film(10000, "All hate Cris", " Good comedy",
                LocalDate.of(2002, 2, 10), 40);

        assertThrows(
                FilmDoesNotExistException.class,
                () -> filmController.updateFilm(film),
                "Такого фильма нет в списке.");

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldGetFilmsList() {

        // добавляем фильмы в список

        Film film1 = new Film(1, "All hate Cris", " Good comedy",
                LocalDate.of(2002, 2, 10), 40);
        filmController.addFilm(film1);

        Film film2 = new Film(1, "All like Tests", " Real comedy",
                LocalDate.of(2023, 7, 19), 12000);
        filmController.addFilm(film2);

        //получаем фильмы из списка

        List<Film> filmsList = filmController.listFilms();
        assertEquals(filmsList.size(), 2, "Список фильмов неверного размера");

    }

    //************************* Тесты на валидацию данных для фильмов *************************
    @Test
    public void shouldPassValidationFilmWithValidData() { //filmData is valid = should pass

        Film film = new Film(1, "All hate Cris", " Good comedy",
                LocalDate.of(2002, 2, 10), 40);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Валидные данные фильма не прошли проверку");

    }

    @Test
    public void shouldFailValidationFilmWithEmptyName() { // filmName is empty

        Film film = new Film(1, "", " Good comedy",
                LocalDate.of(2002, 2, 10), 40);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(),
                "Поле название фильма, содержащее только пробелы, прошло валидацию");
        assertEquals(violations.size(), 2,
                "Неверное количество ошибок при проверке пустого поля с названием фильма");

        for (ConstraintViolation<Film> violation : violations) {

            assertEquals(violation.getMessage(), "не должно быть пустым",
                    "Не содержит сообщения об ошибке 'не должно быть пустым'");

            assertEquals(violation.getInvalidValue(), film.getName(),
                    "Неверно определено невалидное значение: поле с названием фильма");
        }
    }

    @Test
    public void shouldFailValidationFilmWithBlankName() { // fillName is blank

        Film film = new Film(1, " ", "Good comedy",
                LocalDate.of(2002, 2, 10), 40);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Пустое поле с названием фильма прошло валидацию");
        assertEquals(violations.size(), 1,
                "Неверное количество ошибок при проверке пустого поля с названием фильма");

        for (ConstraintViolation<Film> violation : violations) {

            assertEquals(violation.getMessage(), "не должно быть пустым",
                    "Не содержит сообщения об ошибке 'не должно быть пустым'");

            assertEquals(violation.getInvalidValue(), film.getName(),
                    "Неверно определено невалидное значение: поле с названием фильма");
        }
    }


    @Test
    public void shouldFailValidationFilmWithNullName() { // filmName is null

        Film film = new Film(1, null, " Good comedy",
                LocalDate.of(2002, 2, 10), 40);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Поле с названием фильма со значением null прошло валидацию");

        assertEquals(violations.size(), 3,
                "Неверное количество ошибок при проверке поля с названием фильма cо значением null");


        for (ConstraintViolation<Film> violation : violations) {

            assertEquals(violation.getInvalidValue(), film.getName(),
                    "Неверное определено невалидное значение: поле с названием фильма");

            assertTrue(violation.getMessage().equals("не должно быть пустым")
                            || violation.getMessage().equals("не должно равняться null"),
                    "Неверные сообщения о найденных ошибках в поле названия фильма со значением null");
        }
    }

    @Test
    public void shouldFailValidationFilmWithTooLongDescription() { // description is too long

        Film film = new Film(1, "All hate Cris",
                " Good comedy, but it's too long description. it's too long description"
                        + "it's too long description. it's too long description. it's too long description. "
                        + "it's too long description. it's too long description. it's too long description. "
                        + "it's too long description. it's too long description.",
                LocalDate.of(2002, 2, 10), 40);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Поле с описанием фильма, содержащее более 200 символов,"
                + " прошло валидацию");

        assertEquals(violations.size(), 1,
                "Неверное количество ошибок при проверке пустого поля с названием фильма");

        for (ConstraintViolation<Film> violation : violations) {

            assertEquals(violation.getMessage(), "размер должен находиться в диапазоне от 0 до 200",
                    "Не содержит сообщения об ошибке 'размер должен находиться в диапазоне от 0 до 200'");

            assertEquals(violation.getInvalidValue(), film.getDescription(),
                    "Неверно определено невалидное значение: поле с описанием фильма");
        }
    }

    @Test
    public void shouldFailValidationFilmWithNullDescription() { // description is null

        Film film = new Film(1, "All hate Cris", null,
                LocalDate.of(2002, 2, 10), 40);


        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Поле с описанием фильма со значением null"
                + " не прошло валидацию");

        assertEquals(violations.size(), 1,
                "Неверное количество ошибок при проверке поля с описанием фильма cо значением null");

        for (ConstraintViolation<Film> violation : violations) {

            assertEquals(violation.getInvalidValue(), film.getDescription(),
                    "Неверное определено невалидное значение: поле с описанием фильма");

            assertTrue(violation.getMessage().equals("не должно быть пустым")
                            || violation.getMessage().equals("не должно равняться null"),
                    "");
        }

    }

    @Test
    public void shouldFailValidationFilmWithInvalidReleaseDate() { //releaseDate is not valid

        Film film = new Film(1, "All hate Cris", " Good comedy",
                LocalDate.of(1700, 2, 10), 40);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Пустое поле с невалидной датой выхода фильма прошло валидацию");
        assertEquals(violations.size(), 1,
                "Неверное количество ошибок при проверке поля с невалидной датой выхода фильма");

        for (ConstraintViolation<Film> violation : violations) {

            assertEquals(violation.getMessage(), "не должно быть ранее 28-12-1895",
                    "Не содержит сообщения об ошибке 'не должно быть ранее 28-12-1895'");

            assertEquals(violation.getInvalidValue(), film.getReleaseDate(),
                    "Неверно определено невалидное значение: поле с датой выхода фильма");
        }
    }

    @Test
    public void shouldFailValidationFilmWithNullReleaseDate() { //releaseDate is null

        Film film = new Film(1, "All hate Cris", "Good comedy",
                null, 40);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Поле с датой выхода фильма со значением null"
                + " не прошло валидацию");

        assertEquals(violations.size(), 2,
                "Неверное количество ошибок при проверке поля с датой выхода фильма cо значением null");

        for (ConstraintViolation<Film> violation : violations) {

            assertEquals(violation.getInvalidValue(), film.getReleaseDate(),
                    "Неверное определено невалидное значение: поле с датой выхода фильма");

            assertTrue(violation.getMessage().equals("не должно быть ранее 28-12-1895")
                            || violation.getMessage().equals("не должно равняться null"),
                    "Неверные сообщения о найденных ошибках в поле дата выхода фильма со значением null");
        }
    }

    @Test
    public void shouldFailValidationFilmWithNegativeDurationValue() { // duration is negative

        Film film = new Film(1, "All hate Cris", " Good comedy",
                LocalDate.of(2002, 2, 10), (-900));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(),
                "Поле с отрицательным значением продолжительности фильма прошло валидацию");

        assertEquals(violations.size(), 1,
                "Неверное количество ошибок при проверке поля "
                        + "с отрицательным значением продолжительности фильма");

        for (ConstraintViolation<Film> violation : violations) {

            assertEquals(violation.getMessage(), "должно быть больше 0",
                    "Не содержит сообщения об ошибке 'должно быть больше 0'");

            assertEquals(violation.getInvalidValue(), film.getDuration(),
                    "Неверно определено невалидное значение: поле с продолжительностью фильма");
        }
    }

    @Test
    public void shouldFailValidationFilmWithNotPositiveDuration() { // duration is not positive

        Film film = new Film(1, "All hate Cris", " Good comedy",
                LocalDate.of(2002, 2, 10), 0);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(),
                "Поле с нулевым значением продолжительности фильма прошло валидацию");

        assertEquals(violations.size(), 1,
                "Неверное количество ошибок при проверке поля "
                        + "с нулевым значением продолжительности фильма");

        for (ConstraintViolation<Film> violation : violations) {

            assertEquals(violation.getMessage(), "должно быть больше 0",
                    "Не содержит сообщения об ошибке 'должно быть больше 0'");

            assertEquals(violation.getInvalidValue(), film.getDuration(),
                    "Неверно определено невалидное значение: поле с продолжительностью фильма");
        }

    }

    @Test
    public void shouldFailValidationFilmWithNullDuration() { // duration is null

        Film film = new Film(1, "All hate Cris", null,
                LocalDate.of(2002, 2, 10), null);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(),
                "Поле с продолжительностью фильма со значением null прошло валидацию");

        assertEquals(violations.size(), 2,
                "Неверное количество ошибок при проверке поля "
                        + "с продолжительностью фильма cо значением null");

        for (ConstraintViolation<Film> violation : violations) {

            assertEquals(violation.getInvalidValue(), film.getDuration(),
                    "Неверное определено невалидное значение: поле с продолжительностью фильма");

            assertTrue(violation.getMessage().equals("не должно быть пустым")
                            || violation.getMessage().equals("не должно равняться null"),
                    "Неверные сообщения о найденных ошибках в поле "
                            + "продолжительность фильма со значением null");
        }

    }

    //************************* Тесты на валидацию данных пользователя *************************
    @Test
    public void shouldPassValidationUserWithValidData() { // login is valid = should pass

        User user = new User(1, "Alex@yandex.ru", "Alexandr Ivanov", "alex",
                LocalDate.of(1990, 10, 10));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Валидные данные пользователя не прошли проверку");

    }

    @Test
    public void shouldFailValidationUserWithEmptyLogin() { // login is empty

        User user = new User(1, "Alex@yandex.ru", "Alexandr Ivanov", "",
                LocalDate.of(1990, 10, 10));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Пустое поле логин пользователя прошло валидацию");
        assertEquals(violations.size(), 3,
                "Неверное количество ошибок при проверке пустого поля логин пользователя");

        for (ConstraintViolation<User> violation : violations) {

            assertTrue(violation.getMessage().equals("не должно быть пустым")
                            || violation.getMessage().equals("не должно содержать пробелы"),
                    "Неверные сообщения о найденных ошибках в пустом поле логин пользователя");

            assertEquals(violation.getInvalidValue(), user.getLogin(),
                    "Неверно определено невалидное значение: поле логин пользователя");
        }
    }

    @Test
    public void shouldFailValidationUserWithBlankLogin() { // login is blank

        User user = new User(1, "Alex@yandex.ru", "Alexandr Ivanov", " ",
                LocalDate.of(1990, 10, 10));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(),
                "Поле логин пользователя, содержащее только пробелы, прошло валидацию");
        assertEquals(violations.size(), 2,
                "Неверное количество ошибок при проверке пустого поля логин пользователя");

        for (ConstraintViolation<User> violation : violations) {

            assertTrue(violation.getMessage().equals("не должно быть пустым")
                            || violation.getMessage().equals("не должно содержать пробелы"),
                    "Неверные сообщения о найденных ошибках в поле "
                            + "логин пользователя, содержащем только пробелы");

            assertEquals(violation.getInvalidValue(), user.getLogin(),
                    "Неверно определено невалидное значение: поле логин пользователя");
        }
    }

    @Test
    public void shouldFailValidationUserWithNullLogin() { // login is null

        User user = new User(1, "Alex@yandex.ru", "Alexandr Ivanov", null,
                LocalDate.of(1990, 10, 10));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Поле логин пользователя со значением null прошло валидацию");
        assertEquals(violations.size(), 3,
                "Неверное количество ошибок при проверке поля логин пользователя со значением null");

        for (ConstraintViolation<User> violation : violations) {

            assertTrue(violation.getMessage().equals("не должно равняться null")
                            || violation.getMessage().equals("не должно содержать пробелы")
                            || violation.getMessage().equals("не должно быть пустым"),
                    "Неверные сообщения о найденных ошибках в поле "
                            + "логин пользователя со значением null");

            assertEquals(violation.getInvalidValue(), user.getLogin(),
                    "Неверно определено невалидное значение: поле логин пользователя");
        }
    }

    @Test
    public void shouldFailValidationUserWithLoginContainsSpace() { // login contains space

        User user = new User(1, "Alex@yandex.ru", "Alexandr Ivanov", "Alex Alex",
                LocalDate.of(1990, 10, 10));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Поле логин пользователя со значением null прошло валидацию");
        assertEquals(violations.size(), 1,
                "Неверное количество ошибок при проверке поля логин пользователя со значением null");

        for (ConstraintViolation<User> violation : violations) {

            assertEquals(violation.getMessage(), "не должно содержать пробелы",
                    "Неверное сообщение о найденных ошибках в поле логин пользователя,"
                            + "содержащем пробелы");

            assertEquals(violation.getInvalidValue(), user.getLogin(),
                    "Неверно определено невалидное значение: поле логин пользователя");
        }
    }

    @Test
    public void shouldPassValidationUserWithEmptyName() { // userName is Empty = should pass

        User user = new User(1, "Alex@yandex.ru", "", "alex",
                LocalDate.of(1990, 10, 10));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Пустое поля имя пользователя не прошло проверку");

    }

    @Test
    public void shouldPassValidationUserWithBlankName() { // userName is Blank = should pass

        User user = new User(1, "Alex@yandex.ru", " ", "alex",
                LocalDate.of(1990, 10, 10));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Имя пользователя, содержащее только пробелы, не прошло проверку");

    }

    @Test
    public void shouldPassValidationUserWithNullName() { // userName is null = should pass

        User user = new User(1, "Alex@yandex.ru", null, "alex",
                LocalDate.of(1990, 10, 10));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(),
                "Имя пользователя, содержащее значение null, не прошло проверку");

    }

    @Test
    public void shouldFailValidationUserWithInvalidEmail() { // invalid email

        User user = new User(1, "Alex НДЕКС_? @ yandex.ru", "Alex Ivanov", "alex",
                LocalDate.of(1990, 10, 10));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Поле с невалидным значением email прошло валидацию");
        assertEquals(violations.size(), 1,
                "Неверное количество ошибок при проверке поля email с некорректным форматом");

        for (ConstraintViolation<User> violation : violations) {

            assertEquals(violation.getMessage(), "должно иметь формат адреса электронной почты",
                    "Неверное сообщение о найденных ошибках в поле email"
                            + "c некорректными данными");

            assertEquals(violation.getInvalidValue(), user.getEmail(),
                    "Неверно определено невалидное значение: поле email");
        }

    }

    @Test
    public void shouldFailValidationUserWithNullEmail() { //email is null

        User user = new User(1, null, "Alex Ivanov", "alex",
                LocalDate.of(1990, 10, 10));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Поле email со значением null прошло валидацию");
        assertEquals(violations.size(), 1,
                "Неверное количество ошибок при проверке поля email со значением null");

        for (ConstraintViolation<User> violation : violations) {

            assertEquals(violation.getMessage(), "не должно равняться null",
                    "Неверное сообщение о найденных ошибках в поле email"
                            + "cо значением null");

            assertEquals(violation.getInvalidValue(), user.getEmail(),
                    "Неверно определено невалидное значение: поле email");
        }
    }

    @Test
    public void shouldFailValidationUserWithFutureBirthday() { //birthday is in future

        User user = new User(1, "Alex@yandex.ru", "Alex Ivanov", "alex",
                LocalDate.of(2990, 10, 10));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Поле дата рождения с датой в будущем времени прошло валидацию");
        assertEquals(violations.size(), 1,
                "Неверное количество ошибок при проверке поля дата рождения с датой в будущем времени");

        for (ConstraintViolation<User> violation : violations) {

            assertEquals(violation.getMessage(), "должно содержать прошедшую дату или сегодняшнее число",
                    "Неверное сообщение о найденных ошибках в поле дата рождения"
                            + " с датой в будущем времени");

            assertEquals(violation.getInvalidValue(), user.getBirthday(),
                    "Неверно определено невалидное значение: поле дата рождения");
        }
    }

    @Test
    public void shouldFailValidationUserWithNullBirthday() { // birthday is null

        User user = new User(1, "Alex@yandex.ru", "Alex Ivanov", "alex",
                null);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Поле дата рождения со значением null прошло валидацию");
        assertEquals(violations.size(), 1,
                "Неверное количество ошибок при проверке поля дата рождения со значением null");

        for (ConstraintViolation<User> violation : violations) {

            assertEquals(violation.getMessage(), "не должно равняться null",
                    "Неверное сообщение о найденных ошибках в поле email"
                            + "cо значением null");

            assertEquals(violation.getInvalidValue(), user.getBirthday(),
                    "Неверно определено невалидное значение: поле дата рождения");
        }
    }

    @Test
    void contextLoads() {
    }

    //************************* Набор невалидных данных пользователей *************************
    static class UsersArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(

                    //пустой логин

                    Arguments.of(new User(1, "Alex@yandex.ru", "Alexandr Ivanov", "",
                            LocalDate.of(1990, 10, 10))),
                    Arguments.of(new User(2, "Alex@yandex.ru", "Alexandr Ivanov", null,
                            LocalDate.of(1990, 10, 10))),
                    Arguments.of(new User(3, "Alex@yandex.ru", "Alexandr Ivanov", " ",
                            LocalDate.of(1990, 10, 10))),

                    //пустой или невалидный формат email

                    Arguments.of(new User(4, null, "Alexandr Ivanov", "alex",
                            LocalDate.of(1990, 10, 10))),
                    Arguments.of(new User(5, "invalid## @ mail.ru",
                            "Alexandr Ivanov", "alex",
                            LocalDate.of(1990, 10, 10))),

                    //пустая или невалидная дата рождения

                    Arguments.of(new User(6, "Alex@yandex.ru", "Alexandr Ivanov", "alex",
                            null)),
                    Arguments.of(new User(7, "Alex@yandex.ru", "Alexandr Ivanov", "alex",
                            LocalDate.of(2100, 10, 10))));
        }
    }

    //************************* Набор невалидных данных фильмов *************************
    static class FilmsArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(

                    // пустое название фильма

                    Arguments.of(new Film(1, "", " Good comedy",
                            LocalDate.of(2002, 2, 10), 40)),
                    Arguments.of(new Film(1, " ", "Good comedy",
                            LocalDate.of(2002, 2, 10), 40)),
                    Arguments.of(new Film(1, null, " Good comedy",
                            LocalDate.of(2002, 2, 10), 40)),

                    // пустое или слишком длинное описание фильма

                    Arguments.of(new Film(1, "All hate Cris",
                            " Good comedy, but it's too long description. it's too long description"
                                    + "it's too long description. it's too long description."
                                    + " it's too long description. it's too long description. "
                                    + "it's too long description. it's too long description. "
                                    + "it's too long description. it's too long description.",
                            LocalDate.of(2002, 2, 10), 40)),
                    Arguments.of(new Film(1, "All hate Cris", null,
                            LocalDate.of(2002, 2, 10), 40)),

                    // пустая или невалидная дата выхода фильма

                    Arguments.of(new Film(1, "All hate Cris", " Good comedy",
                            LocalDate.of(1700, 2, 10), 40)),
                    Arguments.of(new Film(1, "All hate Cris", " Good comedy",
                            null, 40)),

                    // негативное или нулевое значение продолжительности фильма

                    Arguments.of(new Film(1, "All hate Cris", " Good comedy",
                            LocalDate.of(2002, 2, 10), 0)),
                    Arguments.of(new Film(1, "All hate Cris", " Good comedy",
                            LocalDate.of(2002, 2, 10), -900)));
        }
    }
}
