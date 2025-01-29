package ru.practicum.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.event.Event;
import ru.practicum.event.EventMapper;
import java.util.List;
import java.util.Map;

@UtilityClass
public class CompilationMapper {
    public static Compilation toModelFromDto(NewCompilationDto newDto, List<Event> events) {
        return new Compilation(
                newDto.getPinned(),
                newDto.getTitle(),
                events);
    }

    public static CompilationDto fromModelToDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getEvents().stream()
                        .map((x) -> (EventMapper.fromModelToShortDto(x, Map.of(0L,0L))))
                        .toList(),
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }
}
