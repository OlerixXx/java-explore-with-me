package ru.practicum.event.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum State {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static State toState(String stateAction) {
        switch (stateAction) {
            case "SEND_TO_REVIEW":
                return PENDING;
            case "CANCEL_REVIEW":
                return CANCELED;
            case "PUBLISH_EVENT":
                return PUBLISHED;
            case "REJECT_EVENT":
                return CANCELED;
            default:
                throw new IllegalArgumentException("Unknown state: " + stateAction);
        }
    }

    public static List<State> toStateList(State[] array) {
        return Arrays.stream(array)
                .collect(Collectors.toList());
    }
}
