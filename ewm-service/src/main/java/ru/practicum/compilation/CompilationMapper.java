package ru.practicum.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.EventMapper;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class CompilationMapper {
    public static Compilation toModelFromDto(NewCompilationDto newDto, Set<Event> events) {
        return Compilation.builder()
                .pinned(newDto.isPinned())
                .title(newDto.getTitle())
                .events(events)
                .build();
    }

    public static CompilationDto fromModelToDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getEvents().stream()
                        .map((x) -> (EventMapper.fromModelToShortDto(x, Map.of(0L,0L))))
                        .toList(),
                compilation.getId(),
                compilation.isPinned(),
                compilation.getTitle()
        );
    }
}
