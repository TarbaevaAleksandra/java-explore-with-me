package ru.practicum.category;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;

@UtilityClass
public class CategoryMapper {
    public static Category toModelFromDto(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public static Category toModelFromDto(Long id,NewCategoryDto newCategoryDto) {
        return Category.builder()
                .id(id)
                .name(newCategoryDto.getName())
                .build();
    }

    public static CategoryDto fromModelToDto(Category cat) {
        return new CategoryDto(
                cat.getId(),
                cat.getName()
        );
    }
}
