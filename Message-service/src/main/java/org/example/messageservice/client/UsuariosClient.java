package org.example.messageservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UsuariosClient {
    private final RestTemplate restTemplate;

    public String getUserEmail(Long userId) {
        String url = "http://localhost:8082/users/" + userId + "/email";
        return restTemplate.getForObject(url, String.class);
    }
}
