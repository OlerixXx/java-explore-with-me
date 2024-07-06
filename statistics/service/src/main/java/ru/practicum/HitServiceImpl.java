package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Hit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Hit> cq = cb.createQuery(Hit.class);
        Root<Hit> hit = cq.from(Hit.class);

        Predicate startPredicate = (start != null) ? cb.greaterThanOrEqualTo(hit.get("timestamp"), start) : cb.greaterThanOrEqualTo(hit.get("timestamp"), LocalDateTime.now());
        Predicate endPredicate = (end != null) ? cb.lessThanOrEqualTo(hit.get("timestamp"), end) : cb.lessThanOrEqualTo(hit.get("timestamp"), LocalDateTime.now());
        Predicate uniquePredicate = unique ? hit.get("uri").in(grayList).not() : cb.conjunction();

        cq.where(
                cb.and(
                        startPredicate,
                        endPredicate,
                        uniquePredicate
                )
        );

        if (uris != null && !uris.isEmpty()) {
            Predicate uriPredicate = hit.get("uri").in(uris);
            cq.where(
                    cb.and(
                            startPredicate,
                            endPredicate,
                            uniquePredicate,
                            uriPredicate
                    )
            );
        }

        TypedQuery<Hit> typedQuery = entityManager.createQuery(cq);
        return HitMapper.toStatsDtoList(typedQuery.getResultList());
    }
}