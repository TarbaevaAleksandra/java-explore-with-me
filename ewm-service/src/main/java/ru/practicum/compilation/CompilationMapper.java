package ru.practicum.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.category.Category;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;

@UtilityClass
public class CompilationMapper {
    public static Compilation toModelFromDto(NewCompilationDto newDto) {
        return new Compilation();
    }

    public static Compilation toModelFromDto(UpdateCompilationRequest updateDto) {
        return new Compilation();
    }

    public static CompilationDto fromModelToDto(Compilation newDto) {
        return new CompilationDto();
    }
}
