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

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCategory(@RequestBody CategoryDto categoryDto) {
        Category category=categoryService.createCategory(categoryDto);
        CategoryDto res=CategoryBeanMapper.mapEntityToDto(category);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtil.buildCreatedResponse(res, AppConstants.MSG_CREATED));

    }
}