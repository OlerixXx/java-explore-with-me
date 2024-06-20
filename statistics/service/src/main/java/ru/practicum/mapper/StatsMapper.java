package ru.practicum.mapper;

import ru.practicum.StatsDto;
import ru.practicum.model.Hit;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class StatsMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static StatsDto toStatsDto(Hit hit) {
        return new StatsDto(
                hit.getApp(),
                hit.getUri(),
                1L
        );
    }

    public static List<StatsDto> toListStatsDto(List<Hit> hits) {
        return hits.stream()
                .map(StatsMapper::toStatsDto)
                .collect(Collectors.toList());
    }
}
