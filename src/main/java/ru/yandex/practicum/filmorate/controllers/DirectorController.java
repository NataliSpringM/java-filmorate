package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/directors")
public class DirectorController {
    /* обработка запросов HTTP-клиентов на добавление, обновление, получение и удаление информации о режиссерах
    по адресу http://localhost:8080/directors */

    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    // Запрос 'GET /directors' - список всех режиссёров.
    @GetMapping()
    public Collection<Director> findAllDirectors() {
        return directorService.findAllDirectors();
    }

    // Запрос 'GET /directors/{id}' - получение режиссёра по id.
    @GetMapping("/{id}")
    public Director findDirectorById(@PathVariable Integer id) {
        return directorService.findDirectorById(id);
    }

    // Запрос 'POST /directors' - создание режиссёра.
    @PostMapping()
    public Director addDirector(@Valid @RequestBody Director director) {
        return directorService.addDirector(director);
    }

    // Запрос 'PUT /directors' - изменение режиссёра.
    @PutMapping()
    public Director updateDirector(@Valid @RequestBody Director director) {
        return directorService.updateDirector(director);
    }

    // Запрос 'DELETE /directors/{id}' - удаление режиссёра.
    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable Integer id) {
        directorService.deleteDirector(id);
    }

}
