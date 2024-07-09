package ru.practicum.event.model;

import ru.practicum.exception.custom.SortNotFoundException;

public enum Sorted {
    EVENT_DATE,
    VIEWS;

    public static Sorted toSort(String string) {
        switch (string) {
            case "EVENT_DATE":
                return Sorted.EVENT_DATE;
            case "VIEWS":
                return Sorted.VIEWS;
            default:
                throw new SortNotFoundException(string + " - is not Sort value");
        }
    }
}
