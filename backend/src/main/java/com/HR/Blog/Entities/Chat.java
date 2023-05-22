package com.HR.Blog.Entities;

import com.HR.Blog.Payloads.Status;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table
@Data
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String sender;
    private String senderName;
    private String receiver;
    private String receiverName;
    private String message;
    private LocalDateTime date;
    private Status status;
}

