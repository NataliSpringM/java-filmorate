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
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.ReviewService;
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
    private final ReviewStorage reviewStorage;
    private final ReviewService reviewService;
    private final ReviewLikeStorage reviewLikeStorage;
    private final DirectorStorage directorStorage;
    private final DirectorService directorService;
    User userAlex1;
    User userEgor2;
    User userAnna3;
    User userOlga4;
    Film filmAllHatesCris;
    Film filmTomAndJerry;
    Film filmDiamondHand;
    Film filmSearchTestForALL;
    Mpa mpa;
    Set<FilmGenre> genres;
    Director director1;
    Director director2;
    Director director3;
    Review review1;
    Review review2;
    Review review3;
    Review review4;


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
        director1 = new Director();
        director1.setName("Adam Smith");
        director2 = new Director();
        director2.setName("Adam Brown");
        director3 = new Director();
        director3.setName("Daniel Dan");
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
        filmSearchTestForALL = filmDiamondHand.toBuilder().name("Search test should find ALL").build();
        review1 = Review.builder()
                .content("Content")
                .isPositive(true)
                .build();
        review2 = Review.builder()
                .content("Content2")
                .isPositive(false)
                .userId(1L)
                .filmId(1)
                .build();
        review3 = Review.builder()
                .content("Content3")
                .isPositive(true)
                .userId(1L)
                .filmId(2)
                .build();
        review4 = Review.builder()
                .content("Content3")
                .isPositive(true)
                .userId(2L)
                .filmId(2)
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

    //************************* Тестирование работы сервиса работы с отзывами *************************
    @Test
    public void shouldAddReviewAndFindReviewById() { // добавление отзыва и его получение по id

        User user = userStorage.addUser(userAlex1);
        Film film = filmStorage.addFilm(filmAllHatesCris);

        final Long userId = user.getId();
        final Integer filmId = film.getId();

        Review review = review1.toBuilder().userId(userId).filmId(filmId).build();
        Review newReview = reviewStorage.addReview(review);

        final Integer reviewId = newReview.getReviewId();

        Optional<Review> reviewOptional = Optional.ofNullable(reviewStorage.getReviewById(reviewId));
        assertThat(reviewOptional)
                .hasValueSatisfying(r ->
                        assertThat(r)
                                .hasFieldOrPropertyWithValue("reviewId", reviewId)
                                .hasFieldOrPropertyWithValue("content", "Content")
                                .hasFieldOrPropertyWithValue("isPositive", true)
                                .hasFieldOrPropertyWithValue("userId", userId)
                                .hasFieldOrPropertyWithValue("filmId", filmId));

    }

    @Test
    public void shouldUpdateReview() { // обновление информации о фильме

        User user = userStorage.addUser(userAlex1);
        Film film = filmStorage.addFilm(filmAllHatesCris);

        final Long userId = user.getId();
        final Integer filmId = film.getId();

        Review review = review1.toBuilder().userId(userId).filmId(filmId).build();
        Review newReview = reviewStorage.addReview(review);

        final Integer reviewId = newReview.getReviewId();

        Review updatedReview = newReview.toBuilder().content("UPDATED").build();

        reviewStorage.updateReview(updatedReview);

        Optional<Review> reviewOptional = Optional.ofNullable(reviewStorage.getReviewById(reviewId));
        assertThat(reviewOptional)
                .hasValueSatisfying(r ->
                        assertThat(r)
                                .hasFieldOrPropertyWithValue("reviewId", reviewId)
                                .hasFieldOrPropertyWithValue("content", "UPDATED")
                                .hasFieldOrPropertyWithValue("isPositive", true)
                                .hasFieldOrPropertyWithValue("userId", userId)
                                .hasFieldOrPropertyWithValue("filmId", filmId));

    }


    @Test
    public void shouldListReviews() { // получение списка отзывов

        User user1 = userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);
        Film film1 = filmStorage.addFilm(filmAllHatesCris);
        filmStorage.addFilm(filmDiamondHand);

        final Long user1Id = user1.getId();
        final Long user2Id = user2.getId();
        final Integer film1Id = film1.getId();
        final Integer film2Id = film1.getId();

        Review review1WithData = review1.toBuilder().userId(user1Id).filmId(film1Id).build();
        Review review2WithData = review2.toBuilder().userId(user2Id).filmId(film2Id).build();
        Review review3WithData = review3.toBuilder().userId(user1Id).filmId(film2Id).build();

        Review newReview1 = reviewStorage.addReview(review1WithData);
        Review newReview2 = reviewStorage.addReview(review2WithData);
        Review newReview3 = reviewStorage.addReview(review3WithData);

        final Integer review1Id = newReview1.getReviewId();
        final Integer review2Id = newReview2.getReviewId();
        final Integer review3Id = newReview3.getReviewId();

        List<Review> listReviews = reviewService.listReviews(null, null);

        assertThat(listReviews).asList().hasSize(3);

        assertThat(listReviews).asList().contains(reviewStorage.getReviewById(review1Id));
        assertThat(listReviews).asList().contains(reviewStorage.getReviewById(review2Id));
        assertThat(listReviews).asList().contains(reviewStorage.getReviewById(review3Id));

        assertThat(Optional.of(listReviews.get(0)))
                .hasValueSatisfying(r ->
                        AssertionsForClassTypes.assertThat(r)
                                .hasFieldOrPropertyWithValue("content", "Content"));

        assertThat(Optional.of(listReviews.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("content", "Content2"));

        assertThat(Optional.of(listReviews.get(2)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("content", "Content3"));

    }

    @Test
    public void shouldListReviewsWithLimit() { // получение списка отзывов c ограничением размера по id 1

        User user1 = userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);
        Film film1 = filmStorage.addFilm(filmAllHatesCris);
        Film film2 = filmStorage.addFilm(filmDiamondHand);

        final Long user1Id = user1.getId();
        final Long user2Id = user2.getId();
        final Integer film1Id = film1.getId();
        final Integer film2Id = film2.getId();

        Review review1WithData = review1.toBuilder().userId(user1Id).filmId(film1Id).build();
        Review review2WithData = review2.toBuilder().userId(user2Id).filmId(film2Id).build();
        Review review3WithData = review3.toBuilder().userId(user1Id).filmId(film2Id).build();

        Review newReview1 = reviewStorage.addReview(review3WithData);
        Review newReview2 = reviewStorage.addReview(review2WithData);
        Review newReview3 = reviewStorage.addReview(review1WithData);

        final Integer review1Id = newReview1.getReviewId();
        final Integer review2Id = newReview2.getReviewId();
        final Integer review3Id = newReview3.getReviewId();

        reviewLikeStorage.addLikeToReview(review3Id, user2Id);

        List<Review> listReviews = reviewStorage.listReviews(2, 1);

        assertThat(listReviews).asList().hasSize(1);

        assertThat(listReviews).asList().contains(reviewStorage.getReviewById(review1Id));
        assertThat(listReviews).asList().doesNotContain(reviewStorage.getReviewById(review2Id));
        assertThat(listReviews).asList().doesNotContain(reviewStorage.getReviewById(review3Id));

        assertThat(Optional.of(listReviews.get(0)))
                .hasValueSatisfying(r ->
                        AssertionsForClassTypes.assertThat(r)
                                .hasFieldOrPropertyWithValue("content", "Content3"));

    }


    @Test
    public void shouldListReviewsEmpty() { // получение пустого списка отзывов

        List<Review> listReviews = reviewService.listReviews(null, null);

        assertThat(listReviews).asList().hasSize(0);
        assertThat(listReviews).asList().isEmpty();

    }

    @Test
    public void shouldDeleteReview() { //удаление ревью

        User user = userStorage.addUser(userAlex1);
        Film film = filmStorage.addFilm(filmAllHatesCris);

        final Long userId = user.getId();
        final Integer filmId = film.getId();

        Review review = review1.toBuilder().userId(userId).filmId(filmId).build();

        Review newReview = reviewStorage.addReview(review);
        final Integer reviewId = newReview.getReviewId();

        reviewStorage.deleteReview(reviewId);

        List<Review> listReviews = reviewService.listReviews(null, null);

        assertThat(listReviews).asList().hasSize(0);
        assertThat(listReviews).asList().isEmpty();

    }


    @Test
    public void shouldAddLikeToReview() { // добавление лайка ревью

        userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);
        filmStorage.addFilm(filmAllHatesCris);
        Review review = reviewStorage.addReview(review2);


        final Integer reviewId = review.getReviewId();
        final Long userId = user2.getId();

        reviewLikeStorage.addLikeToReview(reviewId, userId);
        final Integer useful = reviewService.getReviewById(reviewId).getUseful();

        assertThat(useful).isEqualTo(1);

    }

    @Test
    public void shouldAddDislikeToReview() { // добавление дизлайка ревью

        userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);
        filmStorage.addFilm(filmAllHatesCris);
        Review review = reviewStorage.addReview(review2);

        final Integer reviewId = review.getReviewId();
        final Long userId = user2.getId();

        reviewLikeStorage.addDislikeToReview(reviewId, userId);

        final Integer useful = reviewService.getReviewById(reviewId).getUseful();
        assertThat(useful).isEqualTo(-1);

    }

    @Test
    public void shouldDeleteLikeFromReview() { // удаление лайков у ревью

        userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);
        filmStorage.addFilm(filmAllHatesCris);
        Review review = reviewStorage.addReview(review2);

        final Integer reviewId = review.getReviewId();
        final Long userId = user2.getId();

        reviewLikeStorage.addLikeToReview(reviewId, userId);
        reviewLikeStorage.deleteLikeFromReview(reviewId, userId);

        final Integer useful = reviewService.getReviewById(reviewId).getUseful();

        assertThat(useful).isEqualTo(0);

    }

    @Test
    public void shouldDeleteDislikeFromReview() { // удаление дизлайков у ревью

        userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);
        filmStorage.addFilm(filmAllHatesCris);
        Review review = reviewStorage.addReview(review2);

        final Integer reviewId = review.getReviewId();
        final Long userId = user2.getId();

        reviewLikeStorage.addDislikeToReview(reviewId, userId);
        reviewLikeStorage.deleteDislikeFromReview(reviewId, userId);

        final Integer useful = reviewService.getReviewById(reviewId).getUseful();

        assertThat(useful).isEqualTo(0);

    }


    //************************* Тестирование работы сервиса работы с фильмами *************************
    @Test
    public void shouldAddFilmAndFindFilmById() { // добавление фильма и его получение по id

        directorStorage.addDirector(director1);
        filmAllHatesCris.toBuilder().directors(Set.of(director1)).build();
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

    @Test
    public void shouldListMostPopularFilmsByGenreId() { // получение списка наиболее популярных фильмов по id жанра

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

        // получаем список фильмов отсортированных по количеству лайков (ограничение размера - 1 фильм)
        List<Film> listFilms = filmStorage.listMostPopularFilms(1, 2, null);

        // проверяем корректность полученных данных - 1 фильм,

        assertThat(listFilms).asList().hasSize(1);

        // проверяем фильм в списке - с жанром 2 фильм с id 1

        assertThat(listFilms).asList().startsWith(filmStorage.getFilmById(film1.getId()));
        assertThat(listFilms).asList().endsWith(filmStorage.getFilmById(film1.getId()));

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "All hates Cris"));

    }

    @Test
    public void shouldListMostPopularFilmsByYear() { // получение списка наиболее популярных фильмов по году

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

        // получаем список фильмов c запросом по году
        List<Film> listFilms = filmStorage.listMostPopularFilms(1, null, 1978);

        // проверяем корректность полученных данных - 3 фильма,

        assertThat(listFilms).asList().hasSize(1);

        // проверяем фильм в списке - год выпуска совпадает у фильма с id 2

        assertThat(listFilms).asList().startsWith(filmStorage.getFilmById(film2.getId()));

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film).hasFieldOrPropertyWithValue("name", "Diamond hand"));
    }

    @Test
    public void shouldSearchFilmsByTitle() { // поиск фильма по названию

       /* создаем четыре фильма - названия двух из них  "All hates Cris" и "Search test should find ALL"
       и включают подстроку "all" без учета регистра*/
        Film film1 = filmStorage.addFilm(filmAllHatesCris);
        Film film2 = filmStorage.addFilm(filmDiamondHand);
        Film film3 = filmStorage.addFilm(filmTomAndJerry);
        Film film4 = filmStorage.addFilm(filmSearchTestForALL);

        List<String> searchBase = List.of("title", "director");


        // получаем список фильмов, содержащих в названии подстроку "all" без учета регистра
        List<Film> listFilms = filmStorage.listSearchResults("all", searchBase);

        // проверяем корректность полученных данных - 2 фильма,

        assertThat(listFilms).asList().hasSize(2);

        // проверяем порядок фильмов в списке - на первом месте фильм с id 1, на последнем фильм с id 4

        assertThat(listFilms).asList().startsWith(filmStorage.getFilmById(film1.getId()));
        assertThat(listFilms).asList().doesNotContain(filmStorage.getFilmById(film3.getId()));
        assertThat(listFilms).asList().doesNotContain(filmStorage.getFilmById(film2.getId()));
        assertThat(listFilms).asList().endsWith(filmStorage.getFilmById(film4.getId()));

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "All hates Cris"));

        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Search test should find ALL"));


    }

    @Test
    public void shouldSearchFilmsByDirectors() { // поиск фильма по названию

        // создаем три фильма - имена режиссеров двух из них "Adam" включают подстроку "dam" без учета регистра

        Director directorFirst = directorStorage.addDirector(director1);
        Director directorSecond = directorStorage.addDirector(director2);
        Director directorThird = directorStorage.addDirector(director3);
        Film filmAllHatesCrisWithDirector = filmAllHatesCris.toBuilder()
                .directors(Set.of(directorFirst)).build();
        Film filmTomAndJerryWithDirector = filmTomAndJerry.toBuilder()
                .directors(Set.of(directorThird)).build();
        Film filmDiamondHandWithDirector = filmDiamondHand.toBuilder()
                .directors(Set.of(directorSecond, directorThird)).build();
        Film film1 = filmStorage.addFilm(filmAllHatesCrisWithDirector);
        Film film2 = filmStorage.addFilm(filmDiamondHandWithDirector);
        Film film3 = filmStorage.addFilm(filmTomAndJerryWithDirector);

        // ставим лайк второму фильму "Diamond Hand" для проверка сортировки
        User user = userStorage.addUser(userAnna3);
        likeStorage.addLike(film2.getId(), user.getId());

        List<String> searchBase = List.of("director");


        // получаем список фильмов, содержащих в названии подстроку all без учета регистра
        List<Film> listFilms = filmStorage.listSearchResults("daM", searchBase);

        // проверяем корректность полученных данных - 2 фильма,

        assertThat(listFilms).asList().hasSize(2);

        // проверяем порядок фильмов в списке - на первом месте фильм с id 2 - с лайком, на втором фильм с id 1

        assertThat(listFilms).asList().startsWith(filmStorage.getFilmById(film2.getId()));
        assertThat(listFilms).asList().endsWith(filmStorage.getFilmById(film1.getId()));

        assertThat(listFilms).asList().doesNotContain(filmStorage.getFilmById(film3.getId()));

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Diamond hand"));

        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "All hates Cris"));
    }

    @Test
    public void shouldSearchFilmsByDirectorsAndTitle() { // поиск фильма по названию

       /* создаем три фильма
         имя режиссера одного из них "Daniel Dan"
         и название фильма "Diamond Hand" включают подстроку "an" без учета регистра*/

        Director directorFirst = directorStorage.addDirector(director1);
        Director directorSecond = directorStorage.addDirector(director2);
        Director directorThird = directorStorage.addDirector(director3);
        Film filmAllHatesCrisWithDirector = filmAllHatesCris.toBuilder()
                .directors(Set.of(directorFirst)).build();
        Film filmTomAndJerryWithDirector = filmTomAndJerry.toBuilder()
                .directors(Set.of(directorThird)).build();
        Film filmDiamondHandWithDirector = filmDiamondHand.toBuilder()
                .directors(Set.of(directorSecond, directorFirst)).build();
        Film film1 = filmStorage.addFilm(filmAllHatesCrisWithDirector);
        Film film2 = filmStorage.addFilm(filmDiamondHandWithDirector);
        Film film3 = filmStorage.addFilm(filmTomAndJerryWithDirector);

        // ставим два лайка третьему фильму ("Tom and Jerry") для проверка сортировки
        User userEgor = userStorage.addUser(userEgor2);
        User userAnna = userStorage.addUser(userAnna3);
        likeStorage.addLike(film3.getId(), userEgor.getId());
        likeStorage.addLike(film3.getId(), userAnna.getId());

        List<String> searchBase = List.of("title", "director");


        // получаем список фильмов, содержащих в названии или имени режиссера подстроку "an" без учета регистра
        List<Film> listFilms = filmStorage.listSearchResults("An", searchBase);

        // проверяем корректность полученных данных - 2 фильма,

        assertThat(listFilms).asList().hasSize(2);

        // проверяем порядок фильмов в списке - на первом месте фильм с id 3 - с лайками, на втором фильм с id 2

        assertThat(listFilms).asList().startsWith(filmStorage.getFilmById(film3.getId()));
        assertThat(listFilms).asList().endsWith(filmStorage.getFilmById(film2.getId()));

        assertThat(listFilms).asList().doesNotContain(filmStorage.getFilmById(film1.getId()));

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Tom and Jerry"));

        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Diamond hand"));


    }

    @Test
    public void shouldSearchFilmsByTitleWithoutDirector() { // поиск фильма по названию

        // создаем четыре фильма - названия двух из них включают подстроку all без учета регистра
        Film film1 = filmStorage.addFilm(filmAllHatesCris);
        Film film2 = filmStorage.addFilm(filmDiamondHand);
        Film film3 = filmStorage.addFilm(filmTomAndJerry);
        Film film4 = filmStorage.addFilm(filmSearchTestForALL);

        List<String> searchBase = List.of("title");


        // получаем список фильмов, содержащих в названии подстроку all без учета регистра
        List<Film> listFilms = filmStorage.listSearchResults("all", searchBase);

        // проверяем корректность полученных данных - 2 фильма,

        assertThat(listFilms).asList().hasSize(2);

        // проверяем порядок фильмов в списке - на первом месте фильм с id 1, на последнем фильм с id 4

        assertThat(listFilms).asList().startsWith(filmStorage.getFilmById(film1.getId()));
        assertThat(listFilms).asList().doesNotContain(filmStorage.getFilmById(film3.getId()));
        assertThat(listFilms).asList().doesNotContain(filmStorage.getFilmById(film2.getId()));
        assertThat(listFilms).asList().endsWith(filmStorage.getFilmById(film4.getId()));

        assertThat(Optional.of(listFilms.get(0)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "All hates Cris"));

        assertThat(Optional.of(listFilms.get(1)))
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Search test should find ALL"));


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

    @Test
    public void testCommonFilmsBetweenUsersWithLikes() {

        User user1 = userStorage.addUser(userAlex1);
        User user2 = userStorage.addUser(userEgor2);
        Film film = filmStorage.addFilm(filmTomAndJerry);
        userService.addFriend(user1.getId(), user2.getId());
        likeStorage.addLike(film.getId(), user1.getId());
        likeStorage.addLike(film.getId(), user2.getId());
        filmStorage.getCommonFilmsBetweenUsers(user1.getId(), user2.getId());

    }
}