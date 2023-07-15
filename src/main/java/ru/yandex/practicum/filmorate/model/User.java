package ru.yandex.practicum.filmorate.model;


import lombok.Data;


import java.time.LocalDate;

@Data

public class User {

    int id;
    String email;
    String name;
    String login;
    LocalDate birthday;

    public User(int id, String email, String name, String login, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.login = login;
        this.birthday = birthday;
    }
}
