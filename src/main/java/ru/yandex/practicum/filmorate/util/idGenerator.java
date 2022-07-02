package ru.yandex.practicum.filmorate.util;

public class idGenerator {
    private int nextId = 0;

    public int getNewId() {
        return ++nextId;
    }
}
