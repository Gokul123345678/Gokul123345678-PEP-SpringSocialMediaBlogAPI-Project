package com.example.service;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    public Account createAccount(Account account) {
        if (account == null
                || account.getUsername() == null
                || account.getUsername().trim().isEmpty()
                || account.getPassword() == null
                || account.getPassword().length() < 4) {
            return null;
        }
        Optional<Account> existing = accountRepository.findByUsername(account.getUsername());
        if (existing.isPresent()) {
            return null;
        }
        return accountRepository.save(account);
    }
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    public Optional<Account> getAccountById(Integer accountId) {
        return accountRepository.findById(accountId);
    }
    public Account getAccountByUsername(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        return accountOpt.orElse(null);
    }
    public Account validateLogin(String username, String password) {
        if (username == null || password == null) {
            return null;}
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isPresent()) {
            Account acc = accountOpt.get();
            if (acc.getPassword().equals(password)) {
                return acc;
            }
        }
        return null;}
}
