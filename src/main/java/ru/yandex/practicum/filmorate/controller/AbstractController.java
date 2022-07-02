package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NoSuchElementException;
import ru.yandex.practicum.filmorate.model.AbstractModel;
import ru.yandex.practicum.filmorate.util.idGenerator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public abstract class AbstractController<T extends AbstractModel> {
    private final HashMap<Integer, T> data = new HashMap<>();
    private final idGenerator idGen = new idGenerator();

    @GetMapping
    public List<T> getData() {
        return new ArrayList<T>(data.values());
    }

    @PostMapping
    public T create(@Valid @RequestBody T item) {
        validateToCreate(item);
        item.setId(idGen.getNewId());
        data.put(item.getId(), item);
        log.info("Добавлен item {}", item);
        return item;
    }

    @PutMapping
    public T update(@Valid @RequestBody T item) {
        validateToUpdate(item);
        if (data.containsKey(item.getId())) {
            data.put(item.getId(), item);
            log.info("Обновлен item: {}", item);
            return item;
        } else {
            log.info("Нет такого item: {}", item);
            throw new NoSuchElementException("Нет такого фильма!");
        }
    }

    abstract void validateToCreate(T item);
    abstract void validateToUpdate(T item);
}
