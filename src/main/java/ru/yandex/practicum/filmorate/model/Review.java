package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Validated
@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Review {

    // информация об отзыве - id, содержание, оценка (положительный / негативный), id автора, id фильма, рейтинг
    Integer reviewId; // id отзыва
    @NotNull
    @NotBlank
    String content; // содержание отзыва
    @NotNull
    Boolean isPositive; // оценка фильма (положительный / негативный)
    @NotNull
    Long userId; // id пользователя
    @NotNull
    Integer filmId; // id фильма
    Integer useful; // рейтинг полезности

    public Map<String, Object> toMap() {

        Map<String, Object> reviewProperties = new HashMap<>();

        reviewProperties.put("content", content);
        reviewProperties.put("is_positive", isPositive);
        reviewProperties.put("user_id", userId);
        reviewProperties.put("film_id", filmId);

        return reviewProperties;
    }

}