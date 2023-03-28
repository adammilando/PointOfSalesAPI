package com.livecode.ecommerce.service;

import com.livecode.ecommerce.exception.NotFoundException;
import com.livecode.ecommerce.model.Entities.Transaction;
import com.livecode.ecommerce.model.Entities.User;
import com.livecode.ecommerce.model.Request.TransactionRequest;
import com.livecode.ecommerce.repository.TransactionRepository;
import com.livecode.ecommerce.repository.UserRepository;
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
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Page<Transaction> getAllTransaction(
            Integer page,Integer size,
            String direction,String sort
    ){
        try {
            Sort sortBy = Sort.by(Sort.Direction.valueOf(direction), sort);
            Pageable pageable = PageRequest.of((page-1),size,sortBy);
            return transactionRepository.findAll(pageable);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Transaction getTransactionById(Long id){
        try {
            return transactionRepository.findById(id)
                    .orElseThrow(()->
                            new NotFoundException("Report Not Found"));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


    public Transaction createTransaction(TransactionRequest saleTransactionRequest){
        try {
            Transaction transaction = modelMapper.map(transactionRepository, Transaction.class);
            transaction.setTimesTamp(saleTransactionRequest.getTimesTamp());

            Optional<User> userOptional = userRepository.findById(saleTransactionRequest.getUser());
            if (userOptional.isEmpty()){
                throw new NotFoundException("user not found");
            }
            transaction.setUser(userOptional.get());

            return transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Transaction updateTransaction(Long id, TransactionRequest saleTransactionRequest){
        try {
            Transaction transaction = getTransactionById(id);
            transaction.setTimesTamp(saleTransactionRequest.getTimesTamp());
            Optional<User> userOptional = userRepository.findById(saleTransactionRequest.getUser());
            if (userOptional.isEmpty()){
                throw new NotFoundException("user not found");
            }
            transaction.setUser(userOptional.get());
            return transactionRepository.save(transaction);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteTransaction(Long id){
        try {
            Transaction transaction = getTransactionById(id);
            transactionRepository.delete(transaction);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
