package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.Data;

/**
 * информация о директоре - идентификатор, имя
 */
@Data
public class Director {

	@Positive
	private Integer id;
	@NotBlank
	private String name;

}
