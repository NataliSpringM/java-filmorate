package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Event.EventType;
import ru.yandex.practicum.filmorate.model.Event.OperationType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 *  обработка запросов HTTP-клиентов на добавление, обновление, получение информации о пользователях по адресу
 *  http://localhost:8080/users 
 */
@RestController
@Validated
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EventService eventService;
    
    /**
     * обработка POST-запроса на добавление данных пользователя
     * @param user
     * @return
     */
    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {

        return userService.addUser(user);
    }

    /**
     *  обработка PUT-запроса на обновление данных пользователя
     * @param user
     * @return
     */
    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) {

        return userService.updateUser(user);
    }

    /**
     *  обработка GET-запроса на получение списка пользователей
     * @return
     */
    
    @GetMapping()
    public List<User> listUsers() {

        return userService.listUsers();
    }

    /**
     *  обработка GET-запроса на получение пользователя по id
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public User getUserById(@PathVariable Long id) {

        return userService.getUserById(id);
    }

    /**
     *  обработка PUT-запроса на добавление друга
     * @param id
     * @param friendId
     */
    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@RequestBody @PathVariable Long id, @PathVariable Long friendId) {

        userService.addFriend(id, friendId);
        eventService.addEvent(id, friendId, "FRIEND", "ADD");
    }

    /**
     *  обработка DELETE-запроса на добавление друга
     * @param id
     * @param friendId
     */
    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@RequestBody @PathVariable Long id, @PathVariable Long friendId) {

        userService.deleteFriend(id, friendId);
        eventService.addEvent(id, friendId, "FRIEND", "REMOVE");
    }

    /**
     *  обработка GET-запроса на получение списка друзей
     * @param id
     * @return
     */
    @GetMapping("{id}/friends")
    public List<User> listUserFriends(@RequestBody @PathVariable Long id) {

        return userService.listUserFriends(id);
    }

    /**
     *  обработка GET-запроса на получение списка общих друзей
     * @param id
     * @param otherId
     * @return
     */
    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> listCommonFriends(@RequestBody @PathVariable Long id, @PathVariable Long otherId) {

        return userService.listCommonFriends(id, otherId);
    }

    /**
     * обработка GET-запроса на получение ленты событий для пользователя
     * @param id
     * @return
     */
    @GetMapping("{id}/feed")
    public List<Event> listUserEvents(@RequestBody @PathVariable Long id) {
    	return eventService.listUserEvents(id);
    }

}






