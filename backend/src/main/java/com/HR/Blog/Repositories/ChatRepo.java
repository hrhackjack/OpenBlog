package com.HR.Blog.Repositories;

import com.HR.Blog.Entities.Chat;
import com.HR.Blog.Payloads.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepo extends JpaRepository<Chat, Integer> {

    List<Chat> findBySender(String sender);

    List<Chat> findByReceiver(String receiver);

    List<Chat> findByStatus(Status status);

}
