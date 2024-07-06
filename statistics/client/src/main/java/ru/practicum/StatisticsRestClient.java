package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatisticsRestClient extends RestTemplate {
    @Value("http://localhost:9090")
    private String ewmStatsServiceUrl;

    private String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    public void create(String app, String uri, String ip) {
        HitDto hitDto = new HitDto(
                app,
                uri,
                ip,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormat))
        );

        postForLocation(ewmStatsServiceUrl + "/hit", hitDto);
        log.info("Создан запрос: POST {}/hit body={}", ewmStatsServiceUrl, hitDto);
    }

    public ResponseEntity<List<StatsDto>> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("start = {}, end={}, uris ={}, unique = {}", start, end, uris, unique);

        var url = new StringBuilder(ewmStatsServiceUrl + "/stats?");
        Map<String, Object> uriVariables = new HashMap<>();

        if (start != null) {
            var startEncode = start.format(DateTimeFormatter.ofPattern(dateTimeFormat));

            uriVariables.put("start", startEncode);
            url.append("start={start}");
        }

        if (end != null) {
            var endEncode = end.format(DateTimeFormatter.ofPattern(dateTimeFormat));
            uriVariables.put("end", endEncode);
            url.append("&end={end}");
        }

        if (uris != null) {
            uriVariables.put("uris", uris.toArray());
            url.append("&uris={uris}");
        }

        if (unique != null) {
            uriVariables.put("unique", unique);
            url.append("&unique={unique}");
        }
        log.info("Создан запрос: GET {}/stats?start={}&end={}&uris={}&unique={}",
                ewmStatsServiceUrl, uriVariables.get("start"), uriVariables.get("end"),
                uriVariables.get("uris"), uriVariables.get("unique"));

        return exchange(url.toString(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<StatsDto>>() {}, uriVariables);
    }
}
