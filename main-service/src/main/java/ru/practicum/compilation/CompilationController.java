package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.request.CompilationDto;
import ru.practicum.compilation.dto.response.CompilationShortDto;
import ru.practicum.convert.ConvertPageable;
import ru.practicum.validated.Create;
import ru.practicum.validated.Update;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public List<CompilationShortDto> getAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                     @RequestParam(required = false, defaultValue = "0") Integer from,
                                                     @RequestParam(required = false, defaultValue = "10") Integer size) {
        return compilationService.getAllCompilations(pinned, ConvertPageable.toMakePage(from, size));
    }

    @GetMapping("/compilations/{compId}")
    public CompilationShortDto getCompilationsById(@PathVariable Long compId) {
        return compilationService.getCompilationsById(compId);
    }

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationShortDto create(@RequestBody @Validated(Create.class) CompilationDto compilationDto) {
        return compilationService.create(compilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        compilationService.delete(compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationShortDto update(@RequestBody @Validated(Update.class) CompilationDto compilationDto, @PathVariable Long compId) {
        return compilationService.update(compilationDto, compId);
    }
}
