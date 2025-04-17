package com.example.gridproject;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<User> fetchUsers(int offset, int limit) {
        String API_URL = "https://dummyjson.com/users?skip=" + offset + "&limit=" + limit;
        ResponseEntity<UserResponse> response = restTemplate.getForEntity(API_URL, UserResponse.class);

        return response.getBody() != null ? response.getBody().getUsers() : Collections.emptyList();
    }

    public int fetchUserCount() {
        ResponseEntity<UserResponse> response = restTemplate.getForEntity("https://dummyjson.com/users", UserResponse.class);
        return response.getBody() != null ? response.getBody().getTotal() : 0;
    }
}


