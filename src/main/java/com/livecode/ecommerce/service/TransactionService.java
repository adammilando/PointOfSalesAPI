package com.livecode.ecommerce.service;

import com.livecode.ecommerce.exception.NotFoundException;
import com.livecode.ecommerce.model.Entities.Transaction;
import com.livecode.ecommerce.model.Entities.User;
import com.livecode.ecommerce.model.Request.TransactionRequest;
import com.livecode.ecommerce.repository.SaleTransactionRepository;
import com.livecode.ecommerce.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {
    @Autowired
    private SaleTransactionRepository saleTransactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<Transaction> getAllTransaction(){
        try {
            return saleTransactionRepository.findAll();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Transaction getTransactionById(Long id){
        try {
            return saleTransactionRepository.findById(id)
                    .orElseThrow(()->
                            new NotFoundException("Report Not Found"));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


    public Transaction createTransaction(TransactionRequest saleTransactionRequest){
        try {
            Transaction transaction = modelMapper.map(saleTransactionRepository, Transaction.class);
            transaction.setTimesTamp(saleTransactionRequest.getTimesTamp());

            Optional<User> userOptional = userRepository.findById(saleTransactionRequest.getUser());
            if (userOptional.isEmpty()){
                throw new NotFoundException("user not found");
            }
            transaction.setUser(userOptional.get());

            return saleTransactionRepository.save(transaction);
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
            return saleTransactionRepository.save(transaction);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteTransaction(Long id){
        try {
            Transaction transaction = getTransactionById(id);
            saleTransactionRepository.delete(transaction);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
