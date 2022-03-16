package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }


    public void printTransferMenu() {
        System.out.println();
        System.out.println("1: See All Previous Transfers");
        System.out.println("2: See Individual Transfers By Id");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printTEBucksMenu() {
        System.out.println();
        System.out.println("1: See List Of Active Users");
        System.out.println("2: Send Money");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printRequestMenu() {
        System.out.println();
        System.out.println("1: See List of Active Users");
        System.out.println("2: Request Money");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }
    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }
    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }
    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }
    public void printUserMenu(User[] users) {
        System.out.println();
        System.out.println("---------------------------");
        System.out.println("Current Users");
        System.out.printf("%-10s%s%n","ID","Name");
        System.out.println("---------------------------");
        for(User u : users) {
            System.out.printf("%-10s%s%n",u.getId(),u.getUsername());
        }
        System.out.println("---------------------------");
        System.out.println();
    }
    public void printAllTransferMenu(Transfer[] allTransfers) {
        System.out.println();
        System.out.println("---------------------------");
        System.out.println();
        System.out.println("List of Previous Transfers:");
        System.out.printf("%-10s %s %10s%n","From","To","Amount");
        System.out.println("---------------------------");
        for(Transfer transfer : allTransfers) {
          //  System.out.println(transfer);
            System.out.printf("%-10s %s %10s%n", transfer.getAccountTo(), transfer.getAccountFrom(), transfer.getAmount());
        }
        System.out.println("---------------------------");
        System.out.println();
    }

    public void printSingleTransferMenu(Transfer transfer){
        System.out.println();
        System.out.println("Transfer Individual Record Information:");
        System.out.println("-----------------------------------------");
        System.out.printf("%-10s %10s %10s %10s%n","Transfer #","From","To","Amount");
        System.out.printf("%-10s %10s %10s %10s%n",transfer.getTransferId(), transfer.getAccountTo(), transfer.getAccountFrom(), transfer.getAmount());
        System.out.println("-----------------------------------------");
    }

   public void promptForRecipient(){
        System.out.println("Who will you send money to? Please enter their username and press enter to continue: ");

        //System.out.println("Great! And how much money will you be sending " + user.getUsername());
    }


}
