package org.example.messageservice.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.messageservice.model.ContactRequest;
import org.example.messageservice.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final RestTemplate restTemplate;
    private final JavaMailSender mailSender;

    public void sendMessageToOwner(ContactRequest request) {

        // Obtener email del propietario desde microservicio de usuarios
        String ownerEmail = restTemplate.getForObject(
                "http://localhost:8082/users/" + request.getOwnerId() + "/email",
                String.class
        );

        if (ownerEmail == null || ownerEmail.isEmpty()) {
            throw new RuntimeException("No se pudo obtener el email del propietario");
        }

        // Crear mensaje
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(ownerEmail); // Email de Juancito
        message.setSubject(request.getSubject());
        message.setText(
                "Nombre: " + request.getSenderName() +
                        "\nEmail de contacto: " + request.getSenderEmail() +
                        "\nTeléfono: " + request.getSenderPhone() +
                        "\n\nMensaje:\n" + request.getMessageText()
        );

        // Configurar reply-to para que Juancito responda a Pepe
        message.setReplyTo(request.getSenderEmail());

        // Enviar email
        mailSender.send(message);
    }
}
