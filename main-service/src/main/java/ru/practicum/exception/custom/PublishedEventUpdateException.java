package ru.practicum.exception.custom;

public class PublishedEventUpdateException extends IllegalArgumentException {
    public PublishedEventUpdateException() {
        super();
    }

    public PublishedEventUpdateException(String message) {
        super(message);
    }

    public PublishedEventUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public PublishedEventUpdateException(Throwable cause) {
        super(cause);
    }
}