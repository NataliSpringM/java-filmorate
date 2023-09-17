package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/reviews")
@Validated
@RequiredArgsConstructor
public class ReviewController {


    /* обработка запросов HTTP-клиентов на добавление, обновление, получение информации об отзывах по адресу
    http://localhost:8080/reviews */

    private final ReviewService reviewService;

    // обработка POST-запроса на добавление отзыва
    @PostMapping()
    public Review addReview(@Valid @RequestBody Review review) {

        return reviewService.addReview(review);
    }

    // обработка PUT-запроса на обновление отзыва
    @PutMapping()
    public Review updateReview(@Valid @RequestBody Review review) {

        return reviewService.updateReview(review);
    }

    // обработка GET-запроса на получение отзыва по идентификатору
    @GetMapping("/{id}")

    public Review getReviewById(@PathVariable Integer id) {

        return reviewService.getReviewById(id);
    }

    // обработка DELETE-запроса на удаление отзыва
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Integer id) {

        reviewService.deleteReview(id);
    }

    // обработка GET-запроса на получение списка отзывов
    @GetMapping()

    public List<Review> listReviews(@RequestParam(required = false) Integer filmId,
                                    @RequestParam(required = false, defaultValue = "10") Integer count) {

        return reviewService.listReviews(filmId, count);
    }


    // обработка PUT-запроса на добавление лайка отзыву
    @PutMapping("/{reviewId}/like/{userId}")
    public void addLikeToReview(@PathVariable Integer reviewId, @PathVariable Long userId) {

        reviewService.addLikeToReview(reviewId, userId);
    }

    // обработка PUT-запроса на добавление дизлайка отзыву
    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislikeToReview(@PathVariable Integer reviewId, @PathVariable Long userId) {

        reviewService.addDislikeToReview(reviewId, userId);
    }

    // обработка DELETE-запроса на удаление лайка у отзыва
    @DeleteMapping("/{reviewId}/like/{userId}")
    public void deleteLikeFromReview(@PathVariable Integer reviewId, @PathVariable Long userId) {

        reviewService.deleteLikeFromReview(reviewId, userId);
    }

    // обработка DELETE-запроса на удаление дизлайка у отзыва
    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void deleteDislikeFromReview(@PathVariable Integer reviewId, @PathVariable Long userId) {

        reviewService.deleteDislikeFromReview(reviewId, userId);
    }

}