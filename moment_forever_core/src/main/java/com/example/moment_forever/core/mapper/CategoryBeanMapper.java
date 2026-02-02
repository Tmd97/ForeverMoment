package com.example.moment_forever.core.mapper;

import com.example.moment_forever.core.dto.CategoryDto;
import com.example.moment_forever.data.entities.Category;

public class CategoryBeanMapper {
    public static CategoryDto mapEntityToDto(Category category) {
        CategoryDto categoryDto=new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        category.setDesc(category.getDesc());
        return categoryDto;

    }

    public static Category mapDtoToEntity(CategoryDto categoryDto, Category category) {
        category.setName(categoryDto.getName());
        category.setDesc(categoryDto.getDesc());
        category.setEnabled(true);
        return category;

    }
}
