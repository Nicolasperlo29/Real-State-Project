package org.example.messageservice.service;

import org.example.messageservice.model.Message;

import java.util.List;

public interface MessageService {
    public Message send(Message message);
    public List<Message> getMessages(Long userId);
    public void markAsRead(Long messageId);
}
