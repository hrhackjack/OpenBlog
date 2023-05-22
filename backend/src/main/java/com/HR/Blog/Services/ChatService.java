package com.HR.Blog.Services;

import com.HR.Blog.Entities.Chat;
import com.HR.Blog.Payloads.Status;
import com.HR.Blog.Repositories.ChatRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepo chatRepository;

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    public Chat getChatById(Integer id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found with id " + id));
    }

    public List<Chat> getChatsBySenderName(String senderName) {
        return chatRepository.findBySender(senderName);
    }

    public List<Chat> getChatsByReceiverName(String receiverName) {
        return chatRepository.findByReceiver(receiverName);
    }

    public List<Chat> getChatsByStatus(Status status) {
        return chatRepository.findByStatus(status);
    }

    public Chat saveChat(Chat chat) {
        chat.setDate(LocalDateTime.now());
        chat.setStatus(Status.MESSAGE);
        return chatRepository.save(chat);
    }

    public Chat updateChatStatus(Integer id, Status status) {
        Chat chat = getChatById(id);
        chat.setStatus(status);
        return chatRepository.save(chat);
    }

    public void deleteChat(Integer id) {
        chatRepository.deleteById(id);
        updateChatStatus(id, Status.LEAVE);
    }

}

