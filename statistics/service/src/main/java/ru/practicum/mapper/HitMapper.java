package ru.practicum.mapper;

import ru.practicum.HitDto;
import ru.practicum.StatsDto;
import ru.practicum.model.Hit;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HitMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Hit toHit(HitDto hitDto) {
        return new Hit(
                null,
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp() == null ? null : hitDto.getIp(),
                LocalDateTime.parse(hitDto.getTimestamp(), formatter)
        );
    }

    public static List<StatsDto> toStatsDtoList(List<Hit> list) {
        // Группировка по app и uri, подсчет количества хитов
        Map<String, Map<String, Long>> groupedHits = list.stream()
                .collect(Collectors.groupingBy(Hit::getApp,
                        Collectors.groupingBy(Hit::getUri, Collectors.counting())));

        // Преобразование в список StatsDto
        return groupedHits.entrySet().stream()
                .flatMap(appEntry -> appEntry.getValue().entrySet().stream()
                        .map(uriEntry -> new StatsDto(appEntry.getKey(), uriEntry.getKey(), uriEntry.getValue())))
                .collect(Collectors.toList());
    }
}
