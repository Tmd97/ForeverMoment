package com.example.moment_forever.core.services;

import com.example.moment_forever.core.dto.CategoryDto;
import com.example.moment_forever.core.mapper.CategoryBeanMapper;
import com.example.moment_forever.data.dao.CategoryDao;
import com.example.moment_forever.data.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public Category updateCategory(Long id, CategoryDto categoryDto) {

        Category existing = categoryDao.findById(id);
        if (existing == null) {
            return null;
        }

        // map only updatable fields
        CategoryBeanMapper.mapDtoToEntity(categoryDto, existing);

        return categoryDao.update(existing);
    }

    @Transactional(readOnly = true)
    public Category getById(Long id) {
        return categoryDao.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return categoryDao.findAll();
    }

    @Transactional
    public boolean deleteCategory(Long id) {
        Category existing = categoryDao.findById(id);
        if (existing == null) {
            return false;
        }
        categoryDao.delete(existing);
        return true;
    }
}