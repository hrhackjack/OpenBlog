package com.HR.Blog.Controllers;

import com.HR.Blog.Entities.Chat;
import com.HR.Blog.Repositories.UserRepo;
import com.HR.Blog.Services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserRepo userRepo; // Assuming you have a UserRepository

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Chat receiveMessage(@Payload Chat message) {
        setSenderAndReceiverNames(message);
        return message;
    }

    @MessageMapping("/private-message")
    public Chat recMessage(@Payload Chat message) {
        setSenderAndReceiverNames(message);
        simpMessagingTemplate.convertAndSendToUser(message.getReceiver(), "/private", message);
        System.out.println(message);
        return message;
    }

    private void setSenderAndReceiverNames(Chat chat) {
        String sender= chat.getSender();
        String receiver = chat.getReceiver();
        String senderName = userRepo.findNameByEmail(sender);
        String receiverName = userRepo.findNameByEmail(receiver);
        chat.setSenderName(senderName);
        chat.setReceiverName(receiverName);
        chat.setDate(LocalDateTime.now());
    }
}
