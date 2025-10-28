//package org.example.messageservice.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.example.messageservice.model.Message;
//import org.example.messageservice.service.MessageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/messages")
//public class MessageController {
//    @Autowired
//    private MessageService messageService;
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    @MessageMapping("/send")
//    public void sendMessage(Message message) {
//        Message saved = messageService.send(message);
//
//        // Envía al receptor y al emisor
//        messagingTemplate.convertAndSend("/topic/messages/" + message.getReceiverId(), saved);
//        messagingTemplate.convertAndSend("/topic/messages/" + message.getSenderId(), saved);
//    }
//
//
//    @GetMapping("/{userId}")
//    public List<Message> getUserMessages(@PathVariable Long userId) {
//        return messageService.getMessages(userId);
//    }
//}
