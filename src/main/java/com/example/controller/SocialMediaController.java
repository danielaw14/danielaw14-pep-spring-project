package com.example.controller;

import org.hibernate.engine.transaction.jta.platform.internal.ResinJtaPlatform;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import java.util.List;

import javax.websocket.server.PathParam;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.InvalidInputException;
import com.example.exception.LoginFailedException;
import com.example.exception.MessageFailedException;
import com.example.exception.UserDupeException;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(AccountService accountService,MessageService messageService)
    {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public Account postRegister(@RequestBody Account acc) throws UserDupeException, InvalidInputException{
        return accountService.register(acc.getUsername(), acc.getPassword());
    }

    @PostMapping("/login")
    public Account postLogin(@RequestBody Account acc) throws LoginFailedException{
        return accountService.login(acc.getUsername(), acc.getPassword());
    }

    @PostMapping("/messages")
    public Message postMessage(@RequestBody Message m) throws MessageFailedException
    {
        return messageService.postMessage(m.getPostedBy(), m.getMessageText(), m.getTimePostedEpoch());
    }

    @GetMapping("/messages")
    public List<Message> getMessages()
    {
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{messageId}")
    public Message getMessageByMessageId(@PathVariable Integer messageId){
       return messageService.getMessageByMessageId(messageId);
    }

    @DeleteMapping("/messages/{messageId}")
    public int deleteMessageByMessageId(@PathVariable Integer messageId){
        if (messageService.deleteMessageByMessageId(messageId) == 1)
         return messageService.deleteMessageByMessageId(messageId);
        else
            return messageService.deleteMessageByMessageId(messageId);
    }
    @PatchMapping("/messages/{messageId}")
    public int patchMessageByMessageId(@PathVariable Integer messageId, @RequestBody Message m) throws InvalidInputException{
        if (messageService.updateMessage(m.getMessageId(), m) == 1)
            return messageService.updateMessage(messageId, m);
        else{
            throw new InvalidInputException();
        }
    }

    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessageByPostedBy(@PathVariable Integer accountId){
       return messageService.getMessagesByUser(accountId);
    }

}
