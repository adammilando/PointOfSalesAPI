package com.livecode.ecommerce.service;

import com.livecode.ecommerce.exception.NotFoundException;
import com.livecode.ecommerce.model.Entities.Product;
import com.livecode.ecommerce.model.Entities.Transaction;
import com.livecode.ecommerce.model.Entities.SaleDetail;
import com.livecode.ecommerce.model.Request.SalesDetailRequest;
import com.livecode.ecommerce.repository.ProductRepository;
import com.livecode.ecommerce.repository.SaleTrancationItemRepository;
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
public class SalesDetailsService {
    private SaleTrancationItemRepository saleTrancationItemRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private SaleTransactionRepository saleTransactionRepository;
    private ModelMapper modelMapper;

    @Autowired
    public SalesDetailsService(SaleTrancationItemRepository saleTrancationItemRepository, ProductRepository productRepository, UserRepository userRepository, SaleTransactionRepository saleTransactionRepository, ModelMapper modelMapper) {
        this.saleTrancationItemRepository = saleTrancationItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.saleTransactionRepository = saleTransactionRepository;
        this.modelMapper = modelMapper;
    }

    public List<SaleDetail> getAll(){
        try {
            return saleTrancationItemRepository.findAll();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public SaleDetail getSalesById(Long id){
        try {
            return saleTrancationItemRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundException("Transaction With Id " + id + " Not Found"));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public SaleDetail createTransaction(SalesDetailRequest salesDetailRequest){
        try {
            Optional<Product> productOptional= productRepository.findById(salesDetailRequest.getProduct());
            if (productOptional.isEmpty()){
                throw new NotFoundException("Product Not Found");
            }
            Optional<Transaction> saleTransaction = saleTransactionRepository.findById(salesDetailRequest.getTransaction());
            if (saleTransaction.isEmpty()){
                throw new NotFoundException("Sales Not Found");
            }
            Product product = productOptional.get();
            int currentStuck = product.getStock();
            int quantitySold = salesDetailRequest.getQuantity();
            if (currentStuck < quantitySold){
                throw new RuntimeException("Insufficient Stock");
            }
            product.setStock(currentStuck-quantitySold);
            SaleDetail transactionItem = modelMapper.map(salesDetailRequest, SaleDetail.class);
            transactionItem.setProduct(product);
            transactionItem.setTransaction(saleTransaction.get());
            return saleTrancationItemRepository.save(transactionItem);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public SaleDetail updateTransaction(Long id, SalesDetailRequest salesDetailRequest){
        try {
            SaleDetail saleDetail = getSalesById(id);
            Optional<Product> productOptional = productRepository.findById(salesDetailRequest.getProduct());
            if (productOptional.isEmpty()){
                throw new NotFoundException("Product Not Found");
            }
            Product product = productOptional.get();
            int currentStock = product.getStock();
            int quantitySold = salesDetailRequest.getQuantity();
            int difference = saleDetail.getQuantity() - quantitySold;
            if (currentStock + difference < 0) {
                throw new RuntimeException("Insufficient stock");
            }
            product.setStock(currentStock + difference);
            saleDetail.setQuantity(quantitySold);
            saleDetail.setProduct(product);
            productRepository.save(product);
            return saleTrancationItemRepository.save(saleDetail);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


    public void deleteTransaction(Long id){
        try {
            SaleDetail saleDetail = getSalesById(id);
            saleTrancationItemRepository.delete(saleDetail);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
