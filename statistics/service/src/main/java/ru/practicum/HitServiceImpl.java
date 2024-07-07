package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Hit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private static final List<String> grayList = List.of("192.168.0.0", "10.0.0.0", "172.16.0.0");
    @PersistenceContext
    private final EntityManager entityManager;
    private final HitRepository hitRepository;


    @Transactional
    public void create(HitDto hitDto) {
        Hit hit = HitMapper.toHit(hitDto);
        hitRepository.save(hit);
    }

    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (unique) {
            if (uris.isEmpty()) {
                return hitRepository.findStatsByTimeStampBetweenAndUriInAndUniqueWhereUniqueTrue(start, end);
            } else {
                return hitRepository.findStatsByTimeStampBetweenAndUriInAndUniqueWhereUniqueTrue(start, end, uris);
            }
        } else {
            if (uris.isEmpty()) {
                return hitRepository.findStatsByTimeStampBetweenAndUriInAndUniqueWhereUniqueFalse(start, end);
            } else {
                return hitRepository.findStatsByTimeStampBetweenAndUriInAndUniqueWhereUniqueFalse(start, end, uris);
            }
        }
    }
}