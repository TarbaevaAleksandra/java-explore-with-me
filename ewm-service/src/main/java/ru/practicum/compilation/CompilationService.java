package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryMapper;
import ru.practicum.dto.*;

import java.util.List;

@Service
@Getter
@Setter
@AllArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;

    @Transactional
    public CompilationDto saveCompilation(NewCompilationDto compilation) {
        Compilation newCompilation = CompilationMapper.toModelFromDto(compilation);
        return CompilationMapper.fromModelToDto(compilationRepository.save(newCompilation));
    }

    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilation) {
        Compilation newCompilation = CompilationMapper.toModelFromDto(compilation);
        return CompilationMapper.fromModelToDto(compilationRepository.save(newCompilation));
    }

    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pinned,Integer from,Integer size) {
        return List.of(new CompilationDto());
    }
}
