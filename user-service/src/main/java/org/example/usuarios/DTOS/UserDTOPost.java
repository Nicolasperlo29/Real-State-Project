package org.example.usuarios.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOPost {

    private String name;

    private String email;

    private String password;

    private String rol = "user";
}
