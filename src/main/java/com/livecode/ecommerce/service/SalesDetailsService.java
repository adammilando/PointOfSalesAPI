package com.livecode.ecommerce.service;

import com.livecode.ecommerce.exception.NotFoundException;
import com.livecode.ecommerce.model.Entities.Product;
import com.livecode.ecommerce.model.Entities.Transaction;
import com.livecode.ecommerce.model.Entities.SaleDetail;
import com.livecode.ecommerce.model.Request.SalesDetailRequest;
import com.livecode.ecommerce.repository.ProductRepository;
import com.livecode.ecommerce.repository.SalesDetailRepository;
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

import java.util.Optional;

@Service
@Transactional
public class SalesDetailsService {
    private SalesDetailRepository salesDetailRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private ModelMapper modelMapper;

    @Autowired
    public SalesDetailsService(SalesDetailRepository salesDetailRepository, ProductRepository productRepository, UserRepository userRepository, TransactionRepository transactionRepository, ModelMapper modelMapper) {
        this.salesDetailRepository = salesDetailRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.modelMapper = modelMapper;
    }

    public Page<SaleDetail> getAll(
            Integer page,Integer size,
            String direction,String sort){
        try {
            Sort sortBy = Sort.by(Sort.Direction.valueOf(direction), sort);
            Pageable pageable = PageRequest.of((page-1),size,sortBy);
            return salesDetailRepository.findAll(pageable);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public SaleDetail getSalesById(Long id){
        try {
            return salesDetailRepository.findById(id)
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
            Optional<Transaction> saleTransaction = transactionRepository.findById(salesDetailRequest.getTransaction());
            if (saleTransaction.isEmpty()){
                throw new NotFoundException("Sales Not Found");
            }
            SaleDetail transactionItem = modelMapper.map(salesDetailRequest, SaleDetail.class);
            Product product = productOptional.get();
            int currentStuck = product.getStock();
            int quantitySold = salesDetailRequest.getQuantity();
            if (currentStuck < quantitySold){
                throw new RuntimeException("Insufficient Stock");
            }
            product.setStock(currentStuck-quantitySold);
            transactionItem.setProduct(product);
            transactionItem.setTransaction(saleTransaction.get());
            return salesDetailRepository.save(transactionItem);
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
            return salesDetailRepository.save(saleDetail);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


    public void deleteTransaction(Long id){
        try {
            SaleDetail saleDetail = getSalesById(id);
            salesDetailRepository.delete(saleDetail);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
