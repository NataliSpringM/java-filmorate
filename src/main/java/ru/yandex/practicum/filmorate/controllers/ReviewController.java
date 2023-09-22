package ru.yandex.practicum.filmorate.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.ReviewService;

/**
 * обработка запросов HTTP-клиентов на добавление, обновление, получение
 * информации об отзывах по адресу <a href="http://localhost:8080/reviews">...</a>
 */
@RestController
@Slf4j
@RequestMapping("/reviews")
@Validated
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final EventService eventService;

    /**
     * обработка POST-запроса на добавление отзыва
     *
     * @param review объект Review
     * @return объект Review с id
     */
    @PostMapping()
    public Review addReview(@Valid @RequestBody Review review) {

        Review newReview = reviewService.addReview(review);
        eventService.addEvent(newReview.getUserId(), Long.valueOf(newReview.getReviewId()), "REVIEW", "ADD");
        return newReview;
    }

    /**
     * обработка PUT-запроса на обновление отзыва
     *
     * @param review объект Review
     * @return обновленный объект Review
     */
    @PutMapping()
    public Review updateReview(@Valid @RequestBody Review review) {

        Review newReview = reviewService.updateReview(review);
        eventService.addEvent(newReview.getUserId(), Long.valueOf(newReview.getReviewId()), "REVIEW", "UPDATE");
        return newReview;
    }

    /**
     * обработка GET-запроса на получение отзыва по идентификатору
     *
     * @param id id отзыва
     * @return объект Review по id
     */
    @GetMapping("/{id}")

    public Review getReviewById(@PathVariable Integer id) {

        return reviewService.getReviewById(id);
    }

    /**
     * обработка DELETE-запроса на удаление отзыва
     *
     * @param id id объекта Review для удаления
     */
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Integer id) {

        eventService.addEvent(this.getReviewById(id).getUserId(), Long.valueOf(id), "REVIEW", "REMOVE");
        reviewService.deleteReview(id);
    }

    /**
     * обработка GET-запроса на получение списка отзывов
     *
     * @param filmId id фильма
     * @param count  возможное ограничение количества отзывов в списке
     * @return список отзывов
     */
    @GetMapping()

    public List<Review> listReviews(@RequestParam(required = false) Integer filmId,
                                    @RequestParam(required = false, defaultValue = "10") Integer count) {

        return reviewService.listReviews(filmId, count);
    }

    /**
     * обработка PUT-запроса на добавление лайка отзыву
     *
     * @param reviewId id фильма
     * @param userId   id пользователя
     */
    @PutMapping("/{reviewId}/like/{userId}")
    public void addLikeToReview(@PathVariable Integer reviewId, @PathVariable Long userId) {

        reviewService.addLikeToReview(reviewId, userId);
    }

    /**
     * обработка PUT-запроса на добавление дизлайка отзыву
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislikeToReview(@PathVariable Integer reviewId, @PathVariable Long userId) {

        reviewService.addDislikeToReview(reviewId, userId);
    }

    /**
     * обработка DELETE-запроса на удаление лайка у отзыва
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    @DeleteMapping("/{reviewId}/like/{userId}")
    public void deleteLikeFromReview(@PathVariable Integer reviewId, @PathVariable Long userId) {

        reviewService.deleteLikeFromReview(reviewId, userId);
    }

    /**
     * обработка DELETE-запроса на удаление дизлайка у отзыва
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void deleteDislikeFromReview(@PathVariable Integer reviewId, @PathVariable Long userId) {

        reviewService.deleteDislikeFromReview(reviewId, userId);
    }

}