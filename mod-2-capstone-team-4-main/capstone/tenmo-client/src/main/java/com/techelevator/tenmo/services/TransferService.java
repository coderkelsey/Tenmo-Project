package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import io.cucumber.java.bs.A;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {
    public static final String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;
    public AuthenticatedUser currentUser = new AuthenticatedUser();
    public AccountService accountService = new AccountService();

    public Transfer[] getAllTransfers(){
       Transfer[] allTransfer = null;
       // try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "account/transfers", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            allTransfer = response.getBody();
            return allTransfer;
      //  }catch(RestClientResponseException | ResourceAccessException e){
        //    BasicLogger.log(e.getMessage());
       // }
      //  return response.getBody();
    }
    public Transfer getSingleTransfer(int transfer_id){
        Transfer transfer = null;
        ResponseEntity<Transfer>response = restTemplate.exchange(API_BASE_URL+ "account/transfers/all/"+transfer_id, HttpMethod.GET, makeAuthEntity(), Transfer.class);
        transfer = response.getBody();
        return transfer;
    }

   // public Transfer viewPendingRequests()

    public void sendBucks(int recipientId, BigDecimal amount) {
        Transfer sendMoney = new Transfer();
        String RESTUrl = API_BASE_URL + "users/user/" + recipientId;
        User recipient = restTemplate.exchange(API_BASE_URL + "users/user/" + recipientId, HttpMethod.GET, makeAuthEntity(), User.class).getBody();

        if (!(amount.compareTo(BigDecimal.ZERO) > 0)) {
            System.out.println("You cannot send send a negative amount!");

        } else if (recipient.getId().equals(currentUser.getUser().getId())) {
            System.out.println("Cannot transfer funds to yourself.");

        } else if (amount.compareTo(accountService.getBalance()) == 1) {
            System.out.println("Insufficient funds.");
        } else {
            sendMoney.setAccountTo(recipient.getId());
            sendMoney.setAccountFrom(currentUser.getUser().getId());
            sendMoney.setAmount(amount);
            executeTransfer(sendMoney);
            System.out.println("Transaction Completed!");
        }
    }

    public void requestBucks(int recipientId, BigDecimal amount){
        Transfer requestMoney = new Transfer();
        User recipient = restTemplate.exchange(API_BASE_URL + "users/user/" + recipientId, HttpMethod.GET, makeAuthEntity(), User.class).getBody();

        if(!(amount.compareTo(BigDecimal.ZERO) > 0 )){
           System.out.println("You cannot request a negative amount!");
        }
        else if(recipient.getId().equals(currentUser.getUser().getId())){
            System.out.println("Cannot request funds from yourself!");
        }
        else if(amount.compareTo(accountService.getBalance()) == 1){
            System.out.println("Insufficient funds.");
        }
        else{
           requestMoney.setAccountTo(recipient.getId());
            requestMoney.setAccountFrom(currentUser.getUser().getId());
            requestMoney.setAmount(amount);
            executeRequestTransfer(requestMoney);
            System.out.println("Request completed!");
        }
    }

    public void executeTransfer(Transfer transaction){
        //Add a transfer to the transfer table
        //HttpEntity<Transfer> sendRequest = new HttpEntity<>(transaction);
        restTemplate.exchange(API_BASE_URL + "/account/transfers/sendTransfer", HttpMethod.POST, makeAuthTransferEntity(transaction), Transfer.class);
        //Update recipient's account balance
        //Update the sender's account balance
    }

    public void executeRequestTransfer(Transfer transaction){
        restTemplate.exchange(API_BASE_URL + "/account/transfers/requestTransfer", HttpMethod.POST, makeAuthTransferEntity(transaction), Transfer.class);
    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> makeAuthTransferEntity(Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }


}
