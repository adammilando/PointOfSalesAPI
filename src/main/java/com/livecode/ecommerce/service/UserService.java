package com.livecode.ecommerce.service;

import com.livecode.ecommerce.exception.NotFoundException;
import com.livecode.ecommerce.model.Entities.Category;
import com.livecode.ecommerce.model.Entities.User;
import com.livecode.ecommerce.model.Request.CategoryRequest;
import com.livecode.ecommerce.model.Request.UserRequest;
import com.livecode.ecommerce.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<User> getAllUser(
            Integer page,Integer size,
            String direction,String sort
    ){
        try {
            Sort sortBy = Sort.by(Sort.Direction.valueOf(direction), sort);
            Pageable pageable = PageRequest.of((page-1),size,sortBy);
            return userRepository.findAll(pageable);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public User getUserById(Long id){
        try {
            return userRepository.findById(id)
                    .orElseThrow(()->
                            new NotFoundException("User With id "+ id +" Not Found"));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public User createUser(User userRequest){
        try {
            return userRepository.save(userRequest);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public User updateCategory(Long id, UserRequest userRequest){
        try {
            User user = getUserById(id);
            user.setUserName(userRequest.getUserName());
            user.setPhone(userRequest.getPhone());
            return userRepository.save(user);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteUser(Long id){
        try {
            User user = getUserById(id);
            userRepository.delete(user);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
