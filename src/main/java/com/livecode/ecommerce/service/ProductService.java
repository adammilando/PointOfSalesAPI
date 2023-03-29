package com.livecode.ecommerce.service;

import com.livecode.ecommerce.exception.NotFoundException;
import com.livecode.ecommerce.model.Entities.Category;
import com.livecode.ecommerce.model.Entities.Product;
import com.livecode.ecommerce.model.Request.ProductRequest;
import com.livecode.ecommerce.repository.CategoryRepository;
import com.livecode.ecommerce.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Page<Product> getAllProduct(
            Integer page, Integer size,
            String direction, String sort
    ){
        try {
            Sort sortBy = Sort.by(Sort.Direction.valueOf(direction), sort);
            Pageable pageable = PageRequest.of((page-1),size,sortBy);
            return productRepository.findAll(pageable);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Product getProductById(Long id){
        try {
            return productRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundException("Product With id "+ id + " Not Found"));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Product createProduct(ProductRequest productRequest){
        try {
            Optional<Category> categoryOptional = categoryRepository.findById(productRequest.getCategory());
            if (categoryOptional.isEmpty()){
                throw new NotFoundException("Category Not Found");
            }
            Product product = modelMapper.map(productRequest, Product.class);
            product.setCategory(categoryOptional.get());
            return productRepository.save(product);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Product updateProduct(Long id, ProductRequest productDetail){
        try {
            Product product = getProductById(id);
            product.setName(productDetail.getName());
            product.setDescription(productDetail.getDescription());
            product.setStock(productDetail.getStock());

            Category category = new Category();
            product.setCategory(category);

            return productRepository.save(product);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteProduct(Long id){
        try {
            Product product = getProductById(id);
            productRepository.delete(product);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
