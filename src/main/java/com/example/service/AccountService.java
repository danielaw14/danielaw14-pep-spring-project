package com.example.service;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.InvalidInputException;
import com.example.exception.LoginFailedException;
import com.example.exception.UserDupeException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class AccountService {

    AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account register(String username, String password) throws UserDupeException, InvalidInputException{
        if (accountRepository.findAccountByUsername(username) == null 
            && !(username == "") && password.length() >= 4){
            Account account = new Account(username, password);
            accountRepository.save(account);
            return accountRepository.findAccountByAccountId(account.getAccountId());
        }
        else if(accountRepository.findAccountByUsername(username) != null)
        {
            throw new UserDupeException();
        }
        else{
            throw new InvalidInputException();
        }
    }

    public Account login(String username, String password) throws LoginFailedException{
        Account account = accountRepository.findAccountByUsernameAndPassword(username, password);
        if(!(account.equals(null)))
            return accountRepository.findAccountByUsernameAndPassword(username, password);
        else{
            throw new LoginFailedException();
        }
    }
}
