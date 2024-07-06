package ru.practicum.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.practicum.HitDto;
import ru.practicum.StatisticsRestClient;
import ru.practicum.StatsDto;
import ru.practicum.event.EventRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestInterceptor implements HandlerInterceptor {
    private final StatisticsRestClient statisticsClient;
    private final EventRepository eventRepository;
    private final RestTemplate restTemplate;
    private static final String STATISTICS_SERVICE_URL = "http://localhost:9090";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        statisticsClient.create("ewm-main-service", request.getRequestURI(), request.getRemoteAddr());
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//
//        HttpEntity<HitDto> requestEntity = new HttpEntity<>(hitDto, headers);
//        ResponseEntity<String> responseEntity = restTemplate.exchange(STATISTICS_SERVICE_URL + "/hit", HttpMethod.POST, requestEntity, String.class);
//
//        if (responseEntity.getStatusCode().is2xxSuccessful()) {
//            System.out.println("Hit successfully logged");
//        } else {
//            System.out.println("Failed to log hit");
//        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {

        if (request.getRequestURI().startsWith("/events/")) {
            LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime end = LocalDateTime.of(2100, 1, 1, 0, 0);
            List<StatsDto> statsDtoList = statisticsClient.getStats(start, end, List.of(request.getRequestURI()), true).getBody();
            eventRepository.incrementViews(Long.parseLong(request.getRequestURI().replaceFirst("/events/", "")), statsDtoList.get(0).getHits());
        }

//        System.out.println(statisticsClient.getStats(
//                URLEncoder.encode("2000-01-01 01:00:00", StandardCharsets.UTF_8),
//                URLEncoder.encode("2100-01-01 01:00:00", StandardCharsets.UTF_8),
//                new String[] {request.getRequestURI()},
//                false)
//        );

//        if (request.getRequestURI().startsWith("/events/")) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//
//        LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
//        LocalDateTime endDateTime = LocalDateTime.of(2100, 1, 1, 0, 0);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String start = URLEncoder.encode(startDateTime.format(formatter), StandardCharsets.UTF_8.toString());
//        String end = URLEncoder.encode(endDateTime.format(formatter), StandardCharsets.UTF_8.toString());
//
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(STATISTICS_SERVICE_URL + "/stats")
//                .queryParam("start", start)
//                .queryParam("end", end)
//                .queryParam("uris", request.getRequestURI())
//                .queryParam("unique", true);
//        ParameterizedTypeReference<List<StatsDto>> responseType = new ParameterizedTypeReference<List<StatsDto>>() {};
//            ResponseEntity<List<StatsDto>> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), responseType);
//            List<StatsDto> statsDtoList = responseEntity.getBody();
//        eventRepository.incrementViews(Long.parseLong(statsDtoList.get(0).getUri().replaceFirst("/events/", "")), statsDtoList.get(0).getHits());
//        }
    }
}
