package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class UserService {

    public static String API_BASE_URL = "http://localhost:8080/users";
    private RestTemplate restTemplate = new RestTemplate();
    public AuthenticatedUser currentUser= new AuthenticatedUser();
   private String authToken = null;


    public User[] getAllUsers(){
        User[] users = null;
        ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(), User[].class);
        users  = response.getBody();
        return users;
    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }
}
