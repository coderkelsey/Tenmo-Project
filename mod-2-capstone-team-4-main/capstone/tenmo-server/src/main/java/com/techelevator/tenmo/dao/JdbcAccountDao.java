package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;
    private JdbcUserDao userDao;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate, JdbcUserDao userDao){
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;

    }
    // GET ACCOUNT INFORMATION
    @Override
    public Account findAccountByUser(String username){
        Account accountId = null;
        String sql = "SELECT account_id, a.user_id, balance FROM account AS a INNER JOIN tenmo_user as tu ON tu.user_id = a.user_id WHERE username = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        try {
            if (results.next()) {
                accountId = mapToRow(results);
            }
        }catch(DataAccessException e){
            //Change message?
            System.out.println("Account "+ username +" was not found.");
        }
            return accountId;
    }

    // #3 GET USER BALANCE
    @Override
    public BigDecimal getBalance(int account_id) {
        BigDecimal balance = new BigDecimal("0.00");
        String sql = "SELECT balance FROM account WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_id);
        try {
            if (results.next()) {
                // map to Row?
                balance = results.getBigDecimal("balance");
            }
        } catch (DataAccessException e){

            //Change message?
                System.out.println("Account " + account_id + " was not found.");
            }
            return balance;
        }

        public BigDecimal addToBalance(BigDecimal amountTransferred, int id){
        Account account = findAccountByUser(userDao.findByUserId(id).getUsername());
        BigDecimal newBal = account.getBalance().add(amountTransferred);
            System.out.println("The new balance is: " + newBal);
        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";
        try{
            jdbcTemplate.update(sql, newBal,id);
        }catch(DataAccessException dae){
            System.out.println("Sorry! There was an error processing that request.");
            }
        return account.getBalance();

        }

        public BigDecimal subtractFromBalance(BigDecimal amountTransferred, int id){
        Account account = findAccountByUser(userDao.findByUserId(id).getUsername());
        BigDecimal newBal = account.getBalance().subtract(amountTransferred);
        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";
        try{
            jdbcTemplate.update(sql, newBal, id);
        }catch(DataAccessException dae){
            System.out.println("Sorry! There was an error processing that request.");
        } return account.getBalance();
        }
        //#4 GET LIST OF ACCOUNTS
 /*   @Override
    public List<Account> getAllAccounts() {
        List<Account> getAllAcounts = new ArrayList<>();
    //    String sql = "SELECT username FROM tenmo_user AS tu LEFT JOIN account AS a ON tu.user_id = a.user_id;";
     //   String sql = "SELECT username FROM tenmo_user LEFT JOIN account ON tenmo_user.user_id = account.user_id;";

        // WHY DO THE ABOVE TWO NOT WORK?
        String sql = "SELECT * FROM account;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Account account = mapToRow(results);
            getAllAcounts.add(account);
        }
        return getAllAcounts;
    }*/

    private Account mapToRow(SqlRowSet rs){
        Account acct = new Account();
        acct.setAccount_id(rs.getInt("account_id"));
        acct.setUser_id(rs.getInt("user_id"));
        acct.setBalance(rs.getBigDecimal("balance"));
        return acct;
    }
}
/*
    @Override
    public String getUsernameByUserId(int user_id) {
      //  Account accountId = null;
        String sql = "SELECT username FROM tenmo_user WHERE user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user_id);
        try {
            if (results.next()) {
                return results.getString("username");
               // accountId = mapToRow(results);
            }
        }catch(DataAccessException e){
            //Change message?
            System.out.println("Account "+ user_id +" was not found.");
        }
        return results.getString("username");
    }*/