package com.example.controller;

import org.apache.tomcat.jni.User;
import org.hibernate.engine.transaction.jta.platform.internal.ResinJtaPlatform;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import javax.websocket.server.PathParam;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.InvalidInputException;
import com.example.exception.LoginFailedException;
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
    public ResponseEntity<Account> postRegister(String username, String password){
        try{
            return ResponseEntity.ok(accountService.register(username, password));
        }
        catch (UserDupeException u){
            return ResponseEntity.status(409).body(null);
        }
        catch (InvalidInputException i){
            return ResponseEntity.status(400).body(null);
        }
        
    }

    @PostMapping("/login")
    public ResponseEntity<Account> postLogin(String username, String password){
        try{
            return ResponseEntity.ok(accountService.login(username, password));
        }
        catch(LoginFailedException l){
            return ResponseEntity.status(401).body(null);
        }
    }

    @PostMapping("/messages")
    public Message postMessage(Integer postedBy, String messageText)
    {
        return messageService.postMessage(postedBy, messageText, null);
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
    public String deleteMessageByMessageId(@PathVariable Integer messageId){
       return messageService.deleteMessageByMessageId(messageId);
    }

    @PatchMapping("/messages/{messageId}")
    public String patchMessageByMessageId(@PathVariable Integer messageId, String messageText){
       return messageService.updateMessage(messageId, messageText);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessageByPostedBy(@PathVariable Integer accountId){
       return messageService.getMessagesByUser(accountId);
    }

}
