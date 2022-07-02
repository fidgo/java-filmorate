package ru.yandex.practicum.filmorate.util;

import lombok.Setter;

public class idGenerator {
    @Setter
    private int nextId = 0;

    public int getNewId() {
        return ++nextId;
    }
}
