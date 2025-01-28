package ru.practicum.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size) {
        return categoryService.findCats(from,size);
    }

    @GetMapping("/{catId}")
    public CategoryDto findById(@PathVariable Long catId) {
        return categoryService.findCatById(catId);
    }
}
