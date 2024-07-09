package ru.practicum.exception.custom;

public class StatusRequestException extends IllegalArgumentException {

    public StatusRequestException() {
        super();
    }

    public StatusRequestException(String message) {
        super(message);
    }

    public StatusRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatusRequestException(Throwable cause) {
        super(cause);
    }
}
