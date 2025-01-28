package ru.practicum.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;

@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
@Getter
@Setter
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto saveCat(@RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.saveCat(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCat(@PathVariable Long catId) {
        categoryService.deleteCat(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCat(@PathVariable Long catId,
                            @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.updateCat(catId, newCategoryDto);
    }
}
