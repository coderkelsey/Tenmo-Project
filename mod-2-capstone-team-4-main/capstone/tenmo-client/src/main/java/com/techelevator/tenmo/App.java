package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.security.Principal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AccountService accountService = new AccountService();
    private UserService userService = new UserService();
    private final TransferService transferService = new TransferService();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        } else {
            accountService.currentUser = currentUser;
            transferService.currentUser = currentUser;
            transferService.accountService = accountService;
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {

        if (currentUser != null) {
            System.out.println(accountService.getBalance());
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void getSingleTransfer(int transferId) {
        if (currentUser != null) {
            System.out.println(transferService.getSingleTransfer(transferId));
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void viewTransferHistory() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printTransferMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                if (currentUser != null) {
                    Transfer[] transfers = transferService.getAllTransfers();
                    consoleService.printAllTransferMenu(transfers);
                } else {
                    consoleService.printErrorMessage();
                }
            } else if (menuSelection == 2) {
                System.out.println();
                int transfer_id = consoleService.promptForInt("Please enter the transfer ID that you would like to reference:");
                Transfer transfer = transferService.getSingleTransfer(transfer_id);
                if(transferService.getSingleTransfer(transfer_id)==null){
                }
                consoleService.printSingleTransferMenu(transfer);

            /*    Transfer transfer = transferService.getSingleTransfer();
                consoleService.printSingleTransferMenu();*/
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void viewPendingRequests() {
        //if()
    }

    private void viewIndividualTransfers() {
    }

    private void sendBucks() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printTEBucksMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                User[] allUsers = userService.getAllUsers();
                consoleService.printUserMenu(allUsers);
            } else if (menuSelection == 2) {
                System.out.println();
                int recipentId = consoleService.promptForInt("Who would you like to send money to? Please enter their id and press enter to continue (0 to cancel): ");
                BigDecimal amount = consoleService.promptForBigDecimal("Enter amount: ");
                transferService.sendBucks(recipentId, amount);
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void requestBucks() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printRequestMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                User[] allUsers = userService.getAllUsers();
                consoleService.printUserMenu(allUsers);
            } else if (menuSelection == 2) {
                int recipentId = consoleService.promptForInt("Who will you request money from? Please enter their id and press enter to continue (0 to cancel): ");
                BigDecimal amount = consoleService.promptForBigDecimal("Enter amount: ");

                transferService.requestBucks(recipentId, amount);

            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }

        }

    }
}
