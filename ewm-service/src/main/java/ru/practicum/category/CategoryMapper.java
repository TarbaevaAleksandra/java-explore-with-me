package ru.practicum.category;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;

@UtilityClass
public class CategoryMapper {
    public static Category toModelFromDto(NewCategoryDto newCategoryDto) {
        return new Category(
                newCategoryDto.getName()
        );
    }

    public static Category toModelFromDto(Long id,NewCategoryDto newCategoryDto) {
        return new Category(
                id,
                newCategoryDto.getName()
        );
    }

    public static CategoryDto fromModelToDto(Category cat) {
        return new CategoryDto(
                cat.getId(),
                cat.getName()
        );
    }
}
