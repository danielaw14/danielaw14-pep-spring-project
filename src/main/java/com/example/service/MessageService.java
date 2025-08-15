package com.example.service;

import org.apache.tomcat.jni.Time;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.MessageFailedException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository)
    {
        this.messageRepository = messageRepository;
    }

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository)
    {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }


    public Message postMessage(Integer postedBy, String messageText, Long timePostedEpoch) throws MessageFailedException
    {
        if (accountRepository.findAccountByAccountId(postedBy) != null 
            && !messageText.equals("") && messageText.length() <= 255)
        {
            Message message = new Message(postedBy, messageText, timePostedEpoch);
            messageRepository.save(message);
            return messageRepository.findMessageByMessageId(message.getMessageId());
        }
        else
        {
            throw new MessageFailedException();
        }
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageByMessageId(Integer messageId){
        return messageRepository.findMessageByMessageId(messageId);
    }

    public String deleteMessageByMessageId(Integer messageId){
        Message message = messageRepository.findMessageByMessageId(messageId);
        if ( message != null)
        {
            messageRepository.deleteById(Integer.toUnsignedLong(messageId));
            return "(1)";
        }
        
        return "";
    }

    public String updateMessage(Integer messageId, String messageText){
        if(messageRepository.findMessageByMessageId(messageId) != null 
            && !messageText.equals("") && messageText.length() <= 255)
        {
            Message message = messageRepository.findMessageByMessageId(messageId);
            Message updateMessage = new Message(messageId, message.getPostedBy(), messageText, message.getTimePostedEpoch());
            messageRepository.deleteById(Integer.toUnsignedLong(messageId));
            messageRepository.save(updateMessage);
            return "(1)";
        }
        return "";
    }
    
    public List<Message> getMessagesByUser(Integer postedBy){
        return messageRepository.findMessagesByPostedBy(postedBy);
    }

}
