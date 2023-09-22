package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.Collection;

/**
 * обработка запросов HTTP-клиентов на добавление, обновление, получение и
 * удаление информации о режиссерах по адресу <a href="http://localhost:8080/directors">...</a>
 */
@RestController
@Validated
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    /**
     * Запрос 'GET /directors' - список всех режиссёров.
     *
     * @return список режиссеров
     */
    @GetMapping()
    public Collection<Director> findAllDirectors() {
        return directorService.findAllDirectors();
    }


    /**
     * Запрос 'GET /directors/{id}' - получение режиссёра по id.
     *
     * @param id режиссера
     * @return режиссер по id
     */
    @GetMapping("/{id}")
    public Director findDirectorById(@PathVariable Integer id) {
        return directorService.findDirectorById(id);
    }

    /**
     * Запрос 'POST /directors' - создание режиссёра.
     *
     * @param director объект Director
     * @return объект Director с id
     */
    @PostMapping()
    public Director addDirector(@Valid @RequestBody Director director) {
        return directorService.addDirector(director);
    }

    /**
     * Запрос 'PUT /directors' - изменение режиссёра.
     *
     * @param director объект Director
     * @return обновленный объект Director
     */
    @PutMapping()
    public Director updateDirector(@Valid @RequestBody Director director) {
        return directorService.updateDirector(director);
    }

    /**
     * Запрос 'DELETE /directors/{id}' - удаление режиссёра.
     *
     * @param id режиссера
     */
    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable Integer id) {
        directorService.deleteDirector(id);
    }

}
