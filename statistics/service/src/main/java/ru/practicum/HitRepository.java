package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT new ru.practicum.StatsDto(h.app, h.uri, COUNT(h)) FROM Hit h WHERE h.timestamp BETWEEN ?1 AND ?2 AND h.uri IN ?3 GROUP BY h.app, h.uri")
    List<StatsDto> findStatsByTimeStampBetweenAndUriInAndUniqueWhereUniqueFalse(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.StatsDto(h.app, h.uri, COUNT(h)) FROM Hit h WHERE h.timestamp BETWEEN ?1 AND ?2 AND h.uri IN ?3 AND h.ip NOT IN ?4 GROUP BY h.app, h.uri")
    List<StatsDto> findStatsByTimeStampBetweenAndUriInAndUniqueWhereUniqueTrue(LocalDateTime start, LocalDateTime end, List<String> uris, String[] grayList);

    List<Hit> findAllByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);
}