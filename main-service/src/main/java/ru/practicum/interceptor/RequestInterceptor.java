package ru.practicum.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.practicum.StatisticsRestClient;
import ru.practicum.StatsDto;
import ru.practicum.event.EventRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestInterceptor implements HandlerInterceptor {
    private final StatisticsRestClient statisticsClient;
    private final EventRepository eventRepository;
    private static final Pattern EVENT_URL_PATTERN = Pattern.compile("^/events/\\d+$");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        statisticsClient.create("ewm-main-service", request.getRequestURI(), request.getRemoteAddr());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        if (EVENT_URL_PATTERN.matcher(request.getRequestURI()).matches()) {
            LocalDateTime start = LocalDateTime.now().minusYears(10);
            LocalDateTime end = LocalDateTime.now().plusYears(100);
            List<StatsDto> statsDtoList = statisticsClient.getStats(start, end, List.of(request.getRequestURI()), true).getBody();
            eventRepository.incrementViews(Long.parseLong(request.getRequestURI().replaceFirst("/events/", "")), statsDtoList.get(0).getHits());
        }
    }
}
