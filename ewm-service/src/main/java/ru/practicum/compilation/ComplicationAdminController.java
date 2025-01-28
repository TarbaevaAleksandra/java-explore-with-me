package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
    public CompilationDto saveComplication(@RequestBody NewCompilationDto compilation) {
        return compilationService.saveCompilation(compilation);
    }

    @DeleteMapping("/{compId}")
    public void deleteComplication(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateComplication(@PathVariable Long compId,
                                             @RequestBody UpdateCompilationRequest compilation) {
        return compilationService.updateCompilation(compId,compilation);
    }
}
