package com.myfinanceapp.finance_tracker.controller;

import com.myfinanceapp.finance_tracker.model.Transaction;
import com.myfinanceapp.finance_tracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // To tell this is REST controller
@RequestMapping("/api/transactions") // Set the base URL for this API
public class TransactionController {

    @Autowired // let spring auto-injection instance for TransactionRepository
    private TransactionRepository transactionRepository;

    // GET ALL Data
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // GET transaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        return transaction.map(ResponseEntity::ok) // If data found
                .orElse(ResponseEntity.notFound().build()); // If data not found
    }

    // POST (Create) a new transaction
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionRepository.save(transaction);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED); // sent status 201 was Created
    }

    // PUT (Update) an existing transaction
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transactionDetails) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            Transaction existingTransaction = transaction.get();
            existingTransaction.setDescription(transactionDetails.getDescription());
            existingTransaction.setAmount(transactionDetails.getAmount());
            existingTransaction.setType(transactionDetails.getType());
            existingTransaction.setDate(transactionDetails.getDate());
            Transaction updatedTransaction = transactionRepository.save(existingTransaction);
            return ResponseEntity.ok(updatedTransaction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE a transaction
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTransaction(@PathVariable Long id) {
        try {
            transactionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // ส่งสถานะ 204 No Content
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
