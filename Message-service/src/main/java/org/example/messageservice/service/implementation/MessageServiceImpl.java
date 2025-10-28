package org.example.messageservice.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.messageservice.model.Message;
import org.example.messageservice.repository.MessageRepository;
import org.example.messageservice.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository repository;

    // Guarda y devuelve el mensaje
    @Override
    public Message send(Message message) {
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);
        return repository.save(message);
    }

    // Devuelve todos los mensajes de un usuario (enviados o recibidos)
    @Override
    public List<Message> getMessages(Long userId) {
        return repository.findBySenderIdOrReceiverId(userId, userId);
    }

    // Marcar como leído
    @Override
    public void markAsRead(Long messageId) {
        repository.findById(messageId).ifPresent(msg -> {
            msg.setRead(true);
            repository.save(msg);
        });
    }
}
