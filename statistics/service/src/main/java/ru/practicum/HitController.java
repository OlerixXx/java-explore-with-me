package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class HitController {
    private final HitService hitService;

    @PostMapping("/hit")
    public ResponseEntity<String> create(@RequestBody @Validated HitDto hitDto) {
        hitService.create(hitDto);
        return new ResponseEntity<>("Информация сохранена", HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String start,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String end,
                                   @RequestParam(required = false, defaultValue = "") String[] uris,
                                   @RequestParam(required = false, defaultValue = "false") boolean unique) {
        return hitService.getStats(start, end, uris, unique);
    }
}
