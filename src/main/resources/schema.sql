

--создание таблицы users

CREATE TABLE IF NOT EXISTS users (
user_id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
email varchar(320) NOT NULL,
user_name varchar(100),
login varchar(100) NOT NULL,
birthday date NOT NULL
);

-- создание таблицы friendship

CREATE TABLE IF NOT EXISTS friendship (
initiator_id bigint REFERENCES users (user_id) ON DELETE CASCADE,
recipient_id bigint REFERENCES users (user_id) ON DELETE CASCADE,
is_confirmed boolean,
PRIMARY KEY(initiator_id, recipient_id)
);

-- создание таблицы rating_mpa

CREATE TABLE IF NOT EXISTS rating_mpa (
rating_mpa_id int PRIMARY KEY,
rating_mpa_name varchar(10) UNIQUE NOT NULL
);

-- создание таблицы films

CREATE TABLE IF NOT EXISTS films (
film_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
film_name varchar(100) NOT NULL,
description varchar(200) NOT NULL,
release_date date NOT NULL,
duration int NOT NULL,
rating_mpa_id int REFERENCES rating_mpa (rating_mpa_id) ON DELETE RESTRICT
);

-- создание таблицы genres

CREATE TABLE IF NOT EXISTS genres (
genre_id int PRIMARY KEY,
genre_name varchar(20) UNIQUE NOT NULL
);

-- создание таблицы film_genre

CREATE TABLE IF NOT EXISTS film_genres (
film_id int REFERENCES films (film_id) ON DELETE CASCADE,
genre_id int REFERENCES genres (genre_id) ON DELETE RESTRICT,
PRIMARY KEY(film_id, genre_id)
);


--создание таблицы likes

CREATE TABLE IF NOT EXISTS likes (
film_id int REFERENCES films (film_id) ON DELETE CASCADE,
user_id bigint REFERENCES users (user_id) ON DELETE CASCADE,
PRIMARY KEY(film_id, user_id)
);





