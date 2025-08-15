package com.example.service;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class AccountService {

    AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account register(String username, String password){
        if (accountRepository.findAccountByUsername(username) == null 
            && !(username.equals("")) && password.length() >= 4){
            Account account = new Account(username, password);
            accountRepository.save(account);
            return accountRepository.findAccountByAccountId(account.getAccountId());
        }
        else
        {
            return null;
        }
    }

    public Account login(String username, String password){
        return accountRepository.findAccountByUsernameAndPassword(username, password);
    }
}
