
----наполнение данными таблицы rating_mpa
MERGE INTO public.rating_mpa (rating_mpa_id, rating_mpa_name)
VALUES (1, 'G'), (2, 'PG'), (3, 'PG-13'), (4, 'R'), (5, 'NC-17');

------ наполнение данными таблицы genres
MERGE INTO public.genres (genre_id, genre_name)
VALUES (1, 'Комедия'), (2, 'Драма'), (3, 'Мультфильм'), (4, 'Триллер'), (5, 'Документальный'), (6, 'Боевик');







