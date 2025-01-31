package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.DataNotFoundException;
import java.util.*;

@Service
@AllArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    public CompilationDto saveCompilation(NewCompilationDto compilation) {
        Set<Event> events;
        if (compilation.getEvents() != null) {
            events = new HashSet<Event>(eventRepository.findAllByIdIn(compilation.getEvents()));
        } else {
            events = new HashSet<>();
        }
        Compilation newCompilation = CompilationMapper.toModelFromDto(compilation,events);
        return CompilationMapper.fromModelToDto(compilationRepository.save(newCompilation));
    }

    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilation) {
        Compilation oldCompilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new DataNotFoundException("Подборка не найдена"));
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            Set<Event> events = new HashSet<Event>(eventRepository.findAllByIdIn(compilation.getEvents()));
            oldCompilation.setEvents(events);
        }
        if (compilation.getPinned() != null) {
            oldCompilation.setPinned(compilation.getPinned());
        }
        if (compilation.getTitle() != null) {
            oldCompilation.setTitle(compilation.getTitle());
        }
        return CompilationMapper.fromModelToDto(compilationRepository.save(oldCompilation));
    }

    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pinned,Integer from,Integer size) {
        Set<Event> eventSet = new HashSet<>();
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);
        for (Compilation c : compilations) {
            eventSet.addAll(c.getEvents());
        }
        return compilations.stream().map(CompilationMapper::fromModelToDto).toList();
    }

    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new DataNotFoundException("Подборка не найдена"));
        return CompilationMapper.fromModelToDto(compilation);
    }
}