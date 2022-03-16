package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    public static String API_BASE_URL = "http://localhost:8080/account/";
    private RestTemplate restTemplate = new RestTemplate();
    public AuthenticatedUser currentUser= new AuthenticatedUser();


    private String authToken = null;

    public AccountService() {
    }

    public Account getAccount(){
        Account userAccount = null;
        try {
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(), Account.class);
            userAccount = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return userAccount;
    }

    //NOT CURRENTLY BEING USED
    public Account[] getAllAccounts(){
        Account[] accounts = null;
        try {
            ResponseEntity<Account[]> response = restTemplate.exchange(API_BASE_URL+currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Account[].class);
            accounts = response.getBody();
        }catch(RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return accounts;
    }

    public BigDecimal getBalance(){
      return getAccount().getBalance();
    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }
}
