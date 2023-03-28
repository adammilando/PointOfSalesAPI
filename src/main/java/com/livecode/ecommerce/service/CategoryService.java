package com.livecode.ecommerce.service;

import com.livecode.ecommerce.exception.NotFoundException;
import com.livecode.ecommerce.model.Entities.Category;
import com.livecode.ecommerce.model.Request.CategoryRequest;
import com.livecode.ecommerce.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<Category> getAllCategory(){
        try {
            return categoryRepository.findAll();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Category getCategoryById(Long id){
        try {
            return categoryRepository.findById(id)
                    .orElseThrow(()->
                            new NotFoundException("Category With id "+ id +" Not Found"));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Category createCategory(CategoryRequest categoryRequest){
        try {
            Category category = modelMapper.map(categoryRequest, Category.class);
            return categoryRepository.save(category);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Category updateCategory(Long id, CategoryRequest categoryDetail){
        try {
            Category category = getCategoryById(id);
            category.setName(category.getName());
            return categoryRepository.save(category);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteCategory(Long id){
        try {
            Category category = getCategoryById(id);
            categoryRepository.delete(category);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}