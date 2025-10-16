package org.example.usuarios.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long authUserId;
    private String email;
}
