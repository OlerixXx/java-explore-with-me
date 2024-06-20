package ru.practicum;

import java.util.List;

public interface HitService {

    void create(HitDto hitDto);

    List<StatsDto> getStats(String start, String end, String[] uris, boolean unique);
}