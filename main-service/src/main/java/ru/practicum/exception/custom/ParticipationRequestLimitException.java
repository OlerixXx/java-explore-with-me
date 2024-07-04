package ru.practicum.exception.custom;

public class ParticipationRequestLimitException extends IllegalArgumentException {
    public ParticipationRequestLimitException() {
        super();
    }

    public ParticipationRequestLimitException(String message) {
        super(message);
    }

    public ParticipationRequestLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParticipationRequestLimitException(Throwable cause) {
        super(cause);
    }
}
