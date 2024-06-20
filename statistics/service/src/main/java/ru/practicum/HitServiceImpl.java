package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mapper.HitMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String[] grayList = {"192.168.0.0", "10.0.0.0", "172.16.0.0"};
    private final HitRepository hitRepository;

    @Transactional
    public void create(HitDto hitDto) {
        hitRepository.save(HitMapper.toHit(hitDto));
    }

    public List<StatsDto> getStats(String start, String end, String[] uris, boolean unique) {
        if (unique) {
            return hitRepository.findStatsByTimeStampBetweenAndUriInAndUniqueWhereUniqueFalse(LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter), List.of(uris));
        } else {
            return hitRepository.findStatsByTimeStampBetweenAndUriInAndUniqueWhereUniqueTrue(LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter), List.of(uris), grayList);
        }
    }
}