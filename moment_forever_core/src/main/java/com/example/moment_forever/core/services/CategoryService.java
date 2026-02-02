package com.example.moment_forever.core.services;

import com.example.moment_forever.common.response.ApiResponse;
import com.example.moment_forever.common.response.ResponseUtil;
import com.example.moment_forever.common.utils.AppConstants;
import com.example.moment_forever.core.dto.CategoryDto;
import com.example.moment_forever.core.mapper.CategoryBeanMapper;
import com.example.moment_forever.data.dao.CategoryDao;
import com.example.moment_forever.data.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    public Category createCategory(CategoryDto categoryDto) {
        if (categoryDao.existsByName(categoryDto.getName())) {
            throw new IllegalArgumentException("Category with name '" + categoryDto.getName() + "' already exists");
        }
        Category category = new Category();
        CategoryBeanMapper.mapDtoToEntity(categoryDto, category);
        return categoryDao.save(category);
    }
}