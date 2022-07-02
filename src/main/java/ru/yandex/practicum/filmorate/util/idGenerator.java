package ru.yandex.practicum.filmorate.util;

import lombok.Setter;

@Setter
public class idGenerator {
    private int nextId = 0;

    public int getNewId() {
        return ++nextId;
    }
}
