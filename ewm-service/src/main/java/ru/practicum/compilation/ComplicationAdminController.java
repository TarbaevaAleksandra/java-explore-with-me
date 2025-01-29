package ru.practicum.compilation;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;

@RestController
@RequestMapping("/admin/compilations")
@Getter
@Setter
@AllArgsConstructor
public class ComplicationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveComplication(@Valid @RequestBody NewCompilationDto compilation) {
        return compilationService.saveCompilation(compilation);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComplication(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateComplication(@PathVariable Long compId,
                                             @Valid @RequestBody UpdateCompilationRequest compilation) {
        return compilationService.updateCompilation(compId,compilation);
    }
}
