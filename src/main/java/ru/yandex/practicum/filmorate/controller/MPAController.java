package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
@AllArgsConstructor
public class MPAController {

    @Autowired
    private final MPAStorage mpaStorage;

    @GetMapping
    public List<MPA> getMpa() {
        log.info("get /mpa");
        return mpaStorage.getMPA();
    }

    @GetMapping("/{id}")
    public MPA getMpa(@PathVariable int id) {
        log.info("get /mpa/{id}");
        return mpaStorage.getMPA(id);
    }
}
