package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mappers.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DirectorTest {
    @Autowired
    private final DirectorStorage storage;
    @Autowired
    private final DirectorMapper directorMapper;

    @Test
    public void checkCrudOperationsWithValidData() {
        // сделаем режиссера
        Director d1 = Director.builder().name("Federico Fellini").build();

        // запишем его в хранилище - возвратится объект, который там сохранился
        Director d2 = storage.addDirector(d1);
        assertEquals(d1.getName(), d2.getName());

        // прочитаем объекты из хранилища
        Collection<Director> directors = storage.findAllDirectors();
        assertEquals(1, directors.size()); // в контейнере должен быть один объект
        assertTrue(directors.contains(d2));

        // обновим информацию о режиссере
        d2.toBuilder().name("Nikita Michalkov").build();
        Director d3 = storage.updateDirector(d2);
        // проверим возврат метода updateDirector()
        assertEquals(d3, d2);

        d3 = storage.findDirectorById(d2.getId());
        // проверим возврат метода findDirectorById() после обновления
        assertEquals(d3, d2);

        storage.deleteDirector(d2.getId());
        directors = storage.findAllDirectors();
        // проверим отсутствие записей в базе после удаления информации о режиссере
        assertEquals(0, directors.size()); // в контейнере не должно быть объектов
    }

    @Test
    void checkCrudOperationsWithInvalidData() {

        Director d1 = Director.builder().name(" ").build();

        // запишем его в хранилище - возвратится объект, который там сохранился
        Director d2 = storage.addDirector(d1);
        assertNotEquals(d1, d2);

        // прочитаем объекты из хранилища
        Collection<Director> directors = storage.findAllDirectors();
        assertEquals(1, directors.size()); // в контейнере должен быть один объект
        assertTrue(directors.contains(d2));

        // обновим информацию о режиссере
        Director d2Updated = d2.toBuilder().id(30).name("Nikita Michalkov").build(); //TODO

        // проверим возврат метода updateDirector()
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> storage.updateDirector(d2Updated));
        assertEquals("Обновление сведений о режиссере с id=30 не выполнено.", ex.getMessage());

        // проверим возврат метода findDirectorById() после обновления
        ex = assertThrows(ObjectNotFoundException.class, () -> storage.findDirectorById(30));
        assertEquals("Режиссер с id=30 отсутствует в хранилище.", ex.getMessage());

        // проверим отсутствие записей в базе после удаления информации о режиссере
        ex = assertThrows(ObjectNotFoundException.class, () -> storage.deleteDirector(30));
        assertEquals("Режиссер с id=30 отсутствует в хранилище.", ex.getMessage());
    }

}
