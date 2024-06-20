package ru.practicum.mapper;

import ru.practicum.HitDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Hit toHit(HitDto hitDto) {
        return new Hit(
                null,
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp() == null ? null : hitDto.getIp(),
                hitDto.getTimestamp() == null ? null : LocalDateTime.parse(hitDto.getTimestamp(), formatter)
        );
    }
}
