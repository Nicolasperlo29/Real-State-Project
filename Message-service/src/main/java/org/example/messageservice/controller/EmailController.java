package org.example.messageservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.messageservice.model.ContactRequest;
import org.example.messageservice.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<String> sendEmail(@RequestBody ContactRequest request) {
        try {
            emailService.sendMessageToOwner(request);
            return ResponseEntity.ok("Consulta enviada al propietario correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar consulta: " + e.getMessage());
        }
    }
}
