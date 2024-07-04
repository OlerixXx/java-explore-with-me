package ru.practicum.convert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertIntegerArray {
    public static List<Long> toLongList(Integer[] array) {
        return Arrays.stream(array)
                .map(Integer::longValue)
                .collect(Collectors.toList());
    }
}
