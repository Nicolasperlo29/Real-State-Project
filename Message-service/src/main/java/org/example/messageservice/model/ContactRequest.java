package org.example.messageservice.model;

import lombok.Data;

@Data
public class ContactRequest {
    private Long ownerId;          // ID del propietario
    private String propertyTitle;  // título de la propiedad
    private String subject;        // asunto del mensaje
    private String messageText;    // contenido del mensaje
    private String senderName;     // nombre de quien envía
    private String senderEmail;    // email de quien envía
    private String senderPhone;    // teléfono opcional
}
