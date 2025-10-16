package org.example.usuarios.listener;

import org.example.usuarios.config.RabbitMQConfig;
import org.example.usuarios.entity.UserEntity;
import org.example.usuarios.event.UserCreatedEvent;
import org.example.usuarios.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedListener {

    @Autowired
    private UserRepository repository;

    @RabbitListener(
            queues = RabbitMQConfig.QUEUE_NAME,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void handleUserCreated(UserCreatedEvent event) {
        UserEntity user = new UserEntity();
        user.setFullName(event.getFullname());
        user.setAuthUserId(event.getAuthUserId());
        user.setEmail(event.getEmail());
        repository.save(user);

        System.out.println("✅ Perfil creado para: " + event.getEmail());
    }
}
