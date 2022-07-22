package ru.yandex.practicum.filmorate.util;

import lombok.Setter;

@Setter
public class idGenerator {
    private long nextId = 0;

    public long getNewId() {
        return ++nextId;
    }
}
