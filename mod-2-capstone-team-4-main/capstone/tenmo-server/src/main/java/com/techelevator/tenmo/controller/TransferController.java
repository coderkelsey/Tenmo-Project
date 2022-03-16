package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.Services.RestAccountService;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/account/transfers")
@PreAuthorize("isAuthenticated()")
public class TransferController {
    private TransferDao dao;
    private RestAccountService accountService;

    // Finished up the constructor and Request Mapping on this page

    public TransferController(TransferDao dao, RestAccountService accountService) {
        this.dao = dao;
        this.accountService = accountService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers() {
        return dao.getAllList();
    }

    @RequestMapping(value = "/all/{id}", method = RequestMethod.GET)
    public Transfer getOneTransfer(@PathVariable("id") int id) {
        return dao.getSingleTransfer(id);

    /*    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
        public User getUser(@PathVariable("userId") int userId){return userDao.findByUserId(userId);}
*/
    }
    @RequestMapping(value = "/requestTransfer", method = RequestMethod.POST)
    public void requestTransfer(@Valid @RequestBody Transfer requestTransfer){
        dao.requestTransfer(requestTransfer.getAccountFrom(), requestTransfer.getAccountTo(), requestTransfer.getAmount());
    }

    @RequestMapping(value = "/sendTransfer", method = RequestMethod.POST)
    public void createSendTransfer(@Valid @RequestBody Transfer sendTransfer){
      /*  System.out.println("TEST: Account ID From: " + sendTransfer.getAccountFrom());
        System.out.println("TEST: Account ID To: " + sendTransfer.getAccountTo());
        System.out.println("TEST: Amount: " + sendTransfer.getAmount());
*/
        dao.sendTransfer(sendTransfer.getAccountFrom(), sendTransfer.getAccountTo(), sendTransfer.getAmount());
    }



/*
    //This would be a POST to update the transactions
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void newTransaction(Principal principal, @RequestBody Transfer transfer) {
        dao.createTransaction(transfer);
        dao.updateBalance(transfer);

        if (!dao.createTransaction(completedTransfer.getTransferTypeId(), completedTransfer.getTransferStatusId(),
                completedTransfer.getAccountFrom(), completedTransfer.getAccountTo(), completedTransfer.getAmount())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer Request Failed.");
        } else {
            return true;
            // return dao.createTransaction(completedTransfer.getTransferTypeId(),completedTransfer.getTransferStatusId(),
            //       completedTransfer.getAccountFrom(),completedTransfer.getAccountTo(), completedTransfer.getAmount());
        }
    }
*/

  /*      @RequestMapping ( value ="request", method = RequestMethod.POST)
        public void sendMoney(@RequestBody Transfer requestTransfer){
        String results = dao.
            return dao.getSingleTransfer(id);
        }
*/


/*
   //This would be a PUT to update the balance - maybe this would go into Account Controller

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "",method = RequestMethod.PUT)
    public void updateBalance(@Valid @RequestBody Transfer completedTranscation){
        dao.create(completedTranscation);
    }
*/




}
