package ru.practicum;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {

    void create(HitDto hitDto);

    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}