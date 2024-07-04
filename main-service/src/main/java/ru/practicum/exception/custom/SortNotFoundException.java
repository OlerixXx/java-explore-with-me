package ru.practicum.exception.custom;

public class SortNotFoundException extends IllegalArgumentException {
    public SortNotFoundException() {
        super();
    }

    public SortNotFoundException(String message) {
        super(message);
    }

    public SortNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SortNotFoundException(Throwable cause) {
        super(cause);
    }
}
