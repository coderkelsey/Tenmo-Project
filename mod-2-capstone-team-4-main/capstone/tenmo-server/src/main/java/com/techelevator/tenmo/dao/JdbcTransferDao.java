package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.data.relational.core.sql.SQL;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;
    private JdbcAccountDao accountDao;
    private JdbcUserDao userDao;


    public JdbcTransferDao(JdbcTemplate jdbcTemplate, JdbcAccountDao accountDao, JdbcUserDao userDao){

        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }
    public static List<Transfer> listTransfers = new ArrayList<>();

    private static final String SQL_TRANSFER_BASE =
    "SELECT t.transfer_id, tt.transfer_type_desc, ts.transfer_status_desc, t.amount, "+
    "aFrom.account_id as fromAcc, "+
    "aFrom.user_id as fromU, aFrom.balance as fromB, "+
    "aTo.account_id as toAcc, aTo.user_id as toU, aTo.balance as toB "+
    "FROM transfer t "+
    "INNER JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id "+
    "INNER JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id "+
    "INNER JOIN account aFrom on account_from = aFrom.account_id "+
    "INNER JOIN account aTo on account_to = aTo.account_id " +
            "INNER JOIN tenmo_user AS tu ON tu.user_id IN (aFrom.user_id, aTo.user_id)";

    //#6 GET ALL TRANSACTIONS FOR SPECIFIC USER
    @Override
    public List<Transfer> getAllList() {
        List<Transfer> transfers = new ArrayList<>();
       String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount \n" +
               "\t\t\tFROM transfer;";
        //String sql = SQL_TRANSFER_BASE+ " WHERE tu.username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()){
            Transfer transferForList = mapToRow(results);
            transfers.add(transferForList);
        }
        return transfers;
    }

    // #5 INDIVIDUAL TRANSFERS USER COMPLETED
    @Override
    public Transfer getSingleTransfer(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount \n" +
                "\t\t\tFROM transfer\n" +
                "\t\t\tWHERE transfer_id = ?;";
      //  String sql = "SELECT transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE transfer_id = ?;";
       // String sql = SQL_TRANSFER_BASE+ " WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        try{
        if (results.next()){
            transfer = mapToRow(results);
        }
        }catch(Exception e){
                System.out.println("The transfer: " + transferId + " was not found.");
        }
        return transfer;
    }

    // #4 NEW TRANSACTION POSTED
    @Override
    public void createTransaction(Transfer transfer) {
        String sql = SQL_TRANSFER_BASE+"INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?,?,?,?,?);";
        jdbcTemplate.update(SQL_TRANSFER_BASE, transfer.getTransferId(), transfer.getTransferTypeId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    // #4 ADD OR SUBTRACT USERS BALANCE
    public Integer updateBalance(int account_id){
        String sql = SQL_TRANSFER_BASE+"UPDATE account SET account_id = ?, balance =? WHERE account_id = ?;";
        return jdbcTemplate.update(sql,account_id);
    }

    @Override
    public String sendTransfer(int user_from_id, int user_to_id, BigDecimal amount) {
        if (user_from_id == user_to_id) {
            System.out.println("You cannot send money to yourself!");
        }
        Account fromAccount = accountDao.findAccountByUser(userDao.findByUserId(user_from_id).getUsername());
        Account toAccount = accountDao.findAccountByUser(userDao.findByUserId(user_to_id).getUsername());

        BigDecimal testBalance = accountDao.getBalance(fromAccount.getAccount_id());
        BigDecimal testAmount = amount;

        if (amount.compareTo(accountDao.getBalance(fromAccount.getAccount_id())) == -1 && amount.compareTo(new BigDecimal(0)) == 1) {
            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (2,2,?,?,?);";
            jdbcTemplate.update(sql, fromAccount.getAccount_id(), toAccount.getAccount_id(), amount);
            accountDao.addToBalance(amount, user_to_id);
            accountDao.subtractFromBalance(amount, user_from_id);
            return "Transfer complete! Wonderful!!!";
        } else {
            return "Transfer has failed! Oh no! This is due to insufficient funds, not sending an amount greater than 0, or trying to send money to yourself. Please try again!";
        }
    }

    @Override
    public String requestTransfer(int user_from_id, int user_to_id, BigDecimal amount) {
        if (user_from_id == user_to_id) {
            System.out.println("You cannot send money to yourself!");
        }
        Account toAccount = accountDao.findAccountByUser(userDao.findByUserId(user_from_id).getUsername());
        Account fromAccount = accountDao.findAccountByUser(userDao.findByUserId(user_to_id).getUsername());

        BigDecimal testBalance = accountDao.getBalance(fromAccount.getAccount_id());
        BigDecimal testAmount = amount;

        if (amount.compareTo(new BigDecimal(0)) == 1) {
                String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (1, 1, ?, ?, ?);";
            jdbcTemplate.update(sql, fromAccount.getAccount_id(), toAccount.getAccount_id(), amount);
            accountDao.addToBalance(amount, user_from_id);
            accountDao.subtractFromBalance(amount, user_to_id);
             //   jdbcTemplate.update(sql, user_from_id, user_to_id, amount);
                return "Great!! We'll send that request over now! Have a wonderful day!";
            } else {
                return "Oops! Sorry! There was a problem when sending!";
            }
        }

    public String updateTransfer(Transfer transfer, int status_id){
        if(status_id == 3){
            String sql = "UPDATE transfer SET transfer_status = ? WHERE transfer_id = ?";
            jdbcTemplate.update(sql, status_id, transfer.getTransferId());
            return "The transfer has been updated. We're sorry, but the transfer was rejected.";
        }
        if(!(accountDao.getBalance(transfer.getAccountFrom()).compareTo(transfer.getAmount()) == -1)) {
            String sql = "UPDATE transfer SET transfer_status_id =? WHERE transfer_id =?;";
            jdbcTemplate.update(sql, status_id, transfer.getTransferId());
            accountDao.subtractFromBalance(transfer.getAmount(), transfer.getAccountTo());
            accountDao.addToBalance(transfer.getAmount(), transfer.getAccountFrom());
            return "The transfer was successful. Have a nice day!";
        }else{
            return "Insufficient funds for transfer. Sorry!";
        }
    }

    // # REMOVE TRANSACTION
   // public boolean delete(int transferId) {
    //    String sql = "DELETE FROM transfer WHERE transferId=?";
     //   return jdbcTemplate.update(sql, transferId) == 1;
    //}


    private Transfer mapToRow(SqlRowSet rs){
        Transfer trns = new Transfer();
        trns.setTransferId(rs.getInt("transfer_id"));
        trns.setTransferTypeId(rs.getInt("transfer_type_id"));
        trns.setTransferStatusId(rs.getInt("transfer_status_id"));
        trns.setAccountFrom(rs.getInt("account_from"));
        trns.setAccountTo(rs.getInt("account_to"));
        trns.setAmount(rs.getBigDecimal("amount"));
        return trns;
    }
}
