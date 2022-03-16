package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    //Updated list name from list to get All List so that it makes more sense in the controller

    //List<Transfer> getAllList(String username);
    List<Transfer> getAllList();
    Transfer getSingleTransfer(int transferId);
    void createTransaction(Transfer transfer);
    Integer updateBalance(int id);

    String sendTransfer (int user_from_id, int user_to_id, BigDecimal amount);
    String requestTransfer (int user_from_id, int user_to_id, BigDecimal amount);


    //Transfer getId (int transferId);
    //boolean delete(int transferId);
}
