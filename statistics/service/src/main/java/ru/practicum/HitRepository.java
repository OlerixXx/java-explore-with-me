package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT new ru.practicum.StatsDto(h.app, h.uri, COUNT(h.ip)) FROM Hit h WHERE h.timestamp BETWEEN ?1 AND ?2 AND h.uri IN ?3 GROUP BY h.app, h.uri ORDER BY COUNT (*) DESC")
    List<StatsDto> findStatsByTimeStampBetweenAndUriInAndUniqueWhereUniqueFalse(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.StatsDto(h.app, h.uri, COUNT(h.ip)) FROM Hit h WHERE h.timestamp BETWEEN ?1 AND ?2 GROUP BY h.app, h.uri ORDER BY COUNT (*) DESC")
    List<StatsDto> findStatsByTimeStampBetweenAndUriInAndUniqueWhereUniqueFalse(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.StatsDto(h.app, h.uri, COUNT(distinct h.ip)) FROM Hit h WHERE h.timestamp BETWEEN ?1 AND ?2 AND h.uri IN ?3 AND h.ip NOT IN ?4 GROUP BY h.app, h.uri ORDER BY COUNT (distinct h.uri) DESC")
    List<StatsDto> findStatsByTimeStampBetweenAndUriInAndUniqueWhereUniqueTrue(LocalDateTime start, LocalDateTime end, List<String> uris, String[] grayList);

    @Query("SELECT new ru.practicum.StatsDto(h.app, h.uri, COUNT(distinct h.ip)) FROM Hit h WHERE h.timestamp BETWEEN ?1 AND ?2 AND h.ip NOT IN ?3 GROUP BY h.app, h.uri ORDER BY COUNT (distinct h.uri) DESC")
    List<StatsDto> findStatsByTimeStampBetweenAndUriInAndUniqueWhereUniqueTrue(LocalDateTime start, LocalDateTime end, String[] grayList);
}