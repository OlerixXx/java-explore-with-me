package ru.practicum.converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertStringArray {
    public static List<String> toStringList(String[] array) {
        return Arrays.stream(array)
                .collect(Collectors.toList());
    }
}