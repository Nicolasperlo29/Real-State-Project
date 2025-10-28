package org.example.messageservice.controller;


import lombok.RequiredArgsConstructor;
import org.example.messageservice.model.Message;
import org.example.messageservice.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class ChatController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send") // cliente envía a /app/send
    public void sendPrivateMessage(Message message) {
        // guardar en BD
        Message saved = messageService.send(message);

        // enviar al destinatario
        messagingTemplate.convertAndSendToUser(
                message.getReceiverId().toString(), // id del receptor
                "/queue/messages",                  // cola de mensajes
                saved
        );

        // enviar al remitente también (para ver el mensaje inmediatamente)
        messagingTemplate.convertAndSendToUser(
                message.getSenderId().toString(),
                "/queue/messages",
                saved
        );
    }

    @GetMapping("/{userId}")
    public List<Message> getUserMessages(@PathVariable Long userId) {
        return messageService.getMessages(userId);
    }
}
