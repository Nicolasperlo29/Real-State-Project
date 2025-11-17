package org.example.usuarios.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedEvent {

    private String fullName;
    private Long authUserId;
    private String email;
}
