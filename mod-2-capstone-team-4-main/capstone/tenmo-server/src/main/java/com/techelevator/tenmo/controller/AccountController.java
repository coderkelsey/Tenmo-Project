package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.Services.AccountService;
import com.techelevator.tenmo.Services.RestAccountService;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/account")
@PreAuthorize("isAuthenticated()")

public class AccountController {
    private AccountDao dao;
    private RestAccountService accountService;
    private UserDao userDao;
    private TransferController transferController;

    public AccountController(AccountDao dao, RestAccountService accountService, UserDao userDao, TransferController transferController){
        this.dao=dao;
        this.accountService=accountService;
        this.userDao = userDao;
        this.transferController = transferController;
    }

   // @RequestMapping(value = "/list", method = RequestMethod.GET)
   // public List<Account> getAllAccount(){
    //    return dao.getAllAccounts();
   // }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Account getAccount(Principal principal){
        return dao.findAccountByUser(principal.getName());
    }

 /*   @RequestMapping(value = "",method = RequestMethod.PUT)
    public Account updateAccount()*/

  /*  @RequestMapping(value = "", method = RequestMethod.GET)
    private Long getCurrentUserId(Principal principal){
        return userDao.findByUsername(principal.getName()).getId();
    }*/
}

