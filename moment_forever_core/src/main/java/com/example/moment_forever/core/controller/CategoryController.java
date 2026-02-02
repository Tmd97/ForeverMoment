package com.example.moment_forever.core.controller;

import com.example.moment_forever.common.response.ApiResponse;
import com.example.moment_forever.common.response.ResponseUtil;
import com.example.moment_forever.common.utils.AppConstants;
import com.example.moment_forever.core.dto.CategoryDto;
import com.example.moment_forever.core.mapper.CategoryBeanMapper;
import com.example.moment_forever.core.services.CategoryService;
import com.example.moment_forever.data.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCategory(@RequestBody CategoryDto categoryDto) {
        Category category = categoryService.createCategory(categoryDto);
        CategoryDto res = CategoryBeanMapper.mapEntityToDto(category);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtil.buildCreatedResponse(res, AppConstants.MSG_CREATED));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.buildErrorResponse(AppConstants.MSG_NOT_FOUND, HttpStatus.NOT_FOUND));
        }
        CategoryDto res = CategoryBeanMapper.mapEntityToDto(category);

        return ResponseEntity.ok(
                ResponseUtil.buildOkResponse(res, AppConstants.MSG_FETCHED)
        );
    }


    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllCategories() {

        List<Category> categories = categoryService.getAll();

        List<CategoryDto> res = categories.stream()
                .map(CategoryBeanMapper::mapEntityToDto)
                .toList();

        return ResponseEntity.ok(
                ResponseUtil.buildOkResponse(res, AppConstants.MSG_FETCHED)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDto categoryDto) {

        Category updated = categoryService.updateCategory(id, categoryDto);

        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.buildErrorResponse(
                            AppConstants.MSG_NOT_FOUND,
                            HttpStatus.NOT_FOUND));
        }

        CategoryDto res = CategoryBeanMapper.mapEntityToDto(updated);

        return ResponseEntity.ok(
                ResponseUtil.buildOkResponse(res, AppConstants.MSG_UPDATED)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCategory(@PathVariable Long id) {

        boolean deleted = categoryService.deleteCategory(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.buildErrorResponse(
                            AppConstants.MSG_NOT_FOUND,
                            HttpStatus.NOT_FOUND));
        }
        return ResponseEntity.ok(
                ResponseUtil.buildOkResponse(null, AppConstants.MSG_DELETED)
        );
    }

}