package org.example.messageservice.service;

import org.example.messageservice.model.ContactRequest;

public interface EmailService {
    public void sendMessageToOwner(ContactRequest request);
}
