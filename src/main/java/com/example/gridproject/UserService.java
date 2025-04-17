package com.example.gridproject;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_URL = "https://dummyjson.com/users";

    public List<User> fetchUsers() {
        ResponseEntity<UserResponse> response = restTemplate.getForEntity(API_URL, UserResponse.class);
        UserResponse body = response.getBody();

        // Protect against null responses
        if (body != null && body.getUsers() != null) {
            return body.getUsers();
        }

        return Collections.emptyList(); // return empty list if null
    }
}

