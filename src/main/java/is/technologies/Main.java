package is.technologies;

import is.technologies.entities.*;
import is.technologies.entities.builders.BankBuilder;
import is.technologies.entities.builders.ClientBuilder;
import is.technologies.exceptions.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws BankException, CentralBankException, TransactionException, ClientException, AccountException, FileNotFoundException {
        String choice;
        ClientBuilder clientBuilder;
        BankBuilder bankBuilder;
        String bankName;
        double bankAmount;
        double amount;
        int id;
        FileReader fr = new FileReader("//Users/frogge/proggs/techLabs/lab1/src/main/java/is/technologies/input.txt");
        Scanner in = new Scanner(fr);
        while (true) {
            System.out.println("Choose option (Enter a number):\n1.CentralBank\n2.Bank\n3.Client\n4.Account");
            choice = in.nextLine();

            switch (choice) {
                case "1":
                    System.out.println(
                            "\nChoose option (Enter a number):\n1.Add a bank\n2.Add a client\n3.Make transaction\n" +
                                    "4.Undo transaction\n5.Fast-forward time");
                    choice = in.nextLine();
                    switch (choice) {
                        case "1":
                            System.out.println("Enter bank name");
                            bankName = in.nextLine();
                            System.out.println("Enter bank limit for unreliable clients");
                            bankAmount = in.nextDouble();
                            bankBuilder = new BankBuilder(bankName, bankAmount);
                            enterBankSettings(bankBuilder, in);
                            Bank bank = bankBuilder.create();
                            CentralBank.getInstance().addBank(bank);
                            System.out.println("Bank ID: " + bank.getId());
                            break;
                        case "2":
                            System.out.println("Enter client name");
                            String clientName = in.nextLine();
                            System.out.println("Enter surname");
                            String surname = in.nextLine();
                            clientBuilder = new ClientBuilder(clientName, surname);
                            enterClientSettings(clientBuilder, in);
                            Client client = clientBuilder.create();
                            CentralBank.getInstance().addClient(client);
                            System.out.println("Client ID: " + client.getId());
                            break;
                        case "3":
                            System.out.println("\nChoose option (Enter a number):\n1.Withdraw\n2.Deposit\n3.Transfer\n");
                            choice = in.nextLine();
                            switch (choice) {
                                case "1":
                                    System.out.println("Enter account ID");
                                    id = in.nextInt();
                                    System.out.println("Enter amount of money to withdraw");
                                    amount = in.nextDouble();
                                    WithdrawTransaction withdrawTransaction =
                                            new WithdrawTransaction(CentralBank.getInstance().findAccount(id), amount);
                                    if (CentralBank.getInstance().findAccount(id).bank
                                            .handleTransaction(withdrawTransaction)) {
                                        System.out.println("Transaction succeeded\nTransaction ID: " +
                                                withdrawTransaction.getId());
                                    } else {
                                        System.out.println("Transaction failed");
                                    }

                                    break;
                                case "2":
                                    System.out.println("Enter account ID");
                                    id = in.nextInt();
                                    System.out.println("Enter amount of money to deposit");
                                    amount = in.nextDouble();
                                    DepositTransaction depositTransaction =
                                            new DepositTransaction(CentralBank.getInstance().findAccount(id), amount);
                                    if (CentralBank.getInstance().findAccount(id).bank
                                            .handleTransaction(depositTransaction)) {
                                        System.out.println("Transaction succeeded\nTransaction ID: " +
                                                depositTransaction.getId());
                                    } else {
                                        System.out.println("Transaction failed");
                                    }

                                    break;
                                case "3":
                                    System.out.println("Enter sender ID");
                                    int senderId = in.nextInt();
                                    System.out.println("Enter receiver ID");
                                    int receiverId = in.nextInt();
                                    System.out.println("Enter amount of money for transfer");
                                    amount = in.nextDouble();
                                    TransferTransaction transaction = new TransferTransaction(
                                            CentralBank.getInstance().findAccount(senderId),
                                            CentralBank.getInstance().findAccount(receiverId),
                                            amount);
                                    if (CentralBank.getInstance().findAccount(senderId).bank
                                            .handleTransaction(transaction)) {
                                        System.out.println("Transaction succeeded\nTransaction ID: " +
                                                transaction.getId());
                                    } else {
                                        System.out.println("Transaction failed");
                                    }

                                    break;
                                default:
                                    System.out.println("Unexpected token\n");
                                    break;
                            }

                            break;
                        case "4":
                            System.out.println("Enter transaction ID");
                            int transactionId = in.nextInt();
                            Transaction undoTransaction = CentralBank.getInstance().findTransaction(transactionId);
                            if (undoTransaction.getAccount().bank.undoTransaction(undoTransaction)) {
                                System.out.println("Transaction undo succeeded");
                            } else {
                                System.out.println("Transaction failed");
                            }

                            break;
                        case "5":
                            System.out.println("\nEnter how much time you want to skip:\n1.day\n2.month\n3.year");
                            choice = in.nextLine();
                            switch (choice) {
                                case "1":
                                    TimeProvider.getInstance().fastForwardDay();
                                    break;
                                case "2":
                                    TimeProvider.getInstance().fastForwardMonth();
                                    break;
                                case "3":
                                    TimeProvider.getInstance().fastForwardYear();
                                    break;
                                default:
                                    System.out.println("Unexpected token\n");
                                    break;
                            }

                            break;
                        default:
                            System.out.println("Unexpected token\n");
                            break;
                    }

                    break;
                case "2":
                    System.out.println(
                            "Choose option (Enter a number):\n1.Open account\n2.Change settings\n3.Subscribe client\n4.Unsubscribe client");
                    choice = in.nextLine();
                    System.out.println("Enter bank Id");
                    int bankId = in.nextInt();
                    switch (choice) {
                        case "1":
                            System.out.println("Enter client Id");
                            id = in.nextInt();
                            System.out.println("Enter account type:\n1.debit\n2.deposit\n3.credit");
                            String output = in.nextLine();
                            AccountTypes accountType = AccountTypes.Debit;
                            switch (output) {
                                case "1":
                                    accountType = AccountTypes.Debit;
                                    break;
                                case "2":
                                    accountType = AccountTypes.Deposit;
                                    break;
                                case "3":
                                    accountType = AccountTypes.Credit;
                                    break;
                            }

                            Account account = CentralBank.getInstance().findBank(bankId)
                                    .OpenAccount(CentralBank.getInstance().findClient(id), accountType);
                            System.out.println("Account ID: " + account.getId());
                            break;
                        case "2":
                            changeBankSettings(CentralBank.getInstance().findBank(bankId), in);
                            break;
                        case "3":
                            System.out.println("Enter client Id");
                            id = in.nextInt();
                            CentralBank.getInstance().findBank(bankId)
                                    .addSubscriber(CentralBank.getInstance().findClient(id));
                            break;
                        case "4":
                            System.out.println("Enter client Id");
                            id = in.nextInt();
                            CentralBank.getInstance().findBank(bankId)
                                    .unsubscribeClient(CentralBank.getInstance().findClient(id));
                            break;
                        default:
                            System.out.println("Unexpected token\n");
                            break;
                    }

                    break;
                case "3":
                    System.out.println(
                            "\nChoose option (Enter a number):\n1.Add/Change address\n2.Add/Change passport\n");
                    choice = in.nextLine();
                    System.out.println("Enter client Id");
                    id = in.nextInt();
                    switch (choice) {
                        case "1":
                            System.out.println("Enter client's address");
                            CentralBank.getInstance().findClient(id).enterOrChangeAddress(in.nextLine());
                            break;
                        case "2":
                            System.out.println("Enter client's passport number");
                            CentralBank.getInstance().findClient(id)
                                    .enterOrChangePassport(in.nextInt());
                            break;
                        default:
                            System.out.println("Unexpected token\n");
                            break;
                    }

                    break;
                case "4":
                    System.out.println("Enter account Id");
                    id = in.nextInt();
                    System.out.println("Account's balance = " + CentralBank.getInstance().findAccount(id).getBalance());
                    break;
                default:
                    System.out.println("Unexpected token\n");
                    break;
            }
        }
    }

    static void enterBankSettings(BankBuilder bankBuilder, Scanner in) throws BankException {
        String choice;
        while (true) {
            System.out.println("\nEnter required settings (Enter a number):\n1.Debit settings\n" +
                    "2.Deposit settings\n3.Credit settings\n4.Quit");
            choice = in.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("Enter fixed percent");
                    bankBuilder.setDebitPercent(in.nextDouble());
                    break;
                case "2":
                    System.out.println("Enter lower percent (x > 0)");
                    var lowerPercent = in.nextDouble();
                    System.out.println("Enter middle percent (x >= " + lowerPercent + ")");
                    var middlePercent = in.nextDouble();
                    System.out.println("Enter highest percent (x >= " + middlePercent + ")");
                    var highestPercent = in.nextDouble();
                    System.out.println("Enter first limit (x > 0)");
                    var firstLimit = in.nextDouble();
                    System.out.println("Enter second limit (x >= " + firstLimit + ")");
                    var secondLimit = in.nextDouble();
                    bankBuilder.setDepositPercent(lowerPercent, middlePercent, highestPercent, firstLimit, secondLimit);
                    break;
                case "3":
                    System.out.println("Enter commission (x > 0)");
                    var commission = in.nextDouble();
                    System.out.println("Enter lower limit of a balance (x < 0 ");
                    var lowerLimit = in.nextDouble();
                    bankBuilder.setCreditSettings(commission, lowerLimit);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Unexpected token\n");
                    break;
            }
        }
    }

    static void enterClientSettings(ClientBuilder clientBuilder, Scanner in) throws ClientException {
        String choice;
        while (true) {
            System.out.println("\nEnter (Enter a number):\n1.Passport\n" +
                    "2.Address\n3.Quit");
            choice = in.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("Enter passport number");
                    clientBuilder.setPassport(in.nextInt());
                    break;
                case "2":
                    System.out.println("Enter address");
                    clientBuilder.setAddress(in.nextLine());
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Unexpected token\n");
                    break;
            }
        }
    }

    static void changeBankSettings(Bank bank, Scanner in) {
        String choice;
        while (true) {
            System.out.println("\nEnter required settings (Enter a number):\n1.Debit settings\n" +
                    "2.Deposit settings\n3.Credit settings\n4.Quit");
            choice = in.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("Enter fixed percent");
                    bank.changePercentDebit(in.nextDouble());
                    break;
                case "2":
                    System.out.println("\nEnter what you want to change (Enter a number):\n1.Lower percent\n" +
                            "2.Middle percent\n3.Higher percent\n4.Lower limit\n5.Higher limit\n6.Quit");
                    choice = in.nextLine();
                    switch (choice) {
                        case "1":
                            System.out.println("Enter lower percent (x > 0; x <= " +
                                    bank.getDepositSettings().getMiddlePercentDepositAccount());
                            bank.changeLowerPercentDeposit(in.nextDouble());
                            break;
                        case "2":
                            System.out.println("Enter middle percent (x >= " + bank.getDepositSettings().getLowerPercentDepositAccount() +
                                    "; x <= " + bank.getDepositSettings().getHigherPercentDepositAccount() + ")");
                            bank.changeMiddlePercentDeposit(in.nextDouble());
                            break;
                        case "3":
                            System.out.println("Enter higher percent (x >= " + bank.getDepositSettings().getMiddlePercentDepositAccount());
                            bank.changeHigherPercentDeposit(in.nextDouble());
                            break;
                        case "4":
                            System.out.println(
                                    "Enter first limit (x > 0; x <=)" + bank.getDepositSettings().getSecondLimitDepositAccount());
                            bank.changeFirstLimitDeposit(in.nextDouble());
                            break;
                        case "5":
                            System.out.println("Enter second limit (x >=)" + bank.getDepositSettings().getFirstLimitDepositAccount());
                            bank.changeSecondLimitDeposit(in.nextDouble());
                            break;
                        case "6":
                            break;
                        default:
                            System.out.println("Unexpected token\n");
                            break;
                    }

                    break;
                case "3":
                    System.out.println(
                            "\nEnter what you want to change (Enter a number):\n1.Credit commission\n" +
                                    "2.Credit lower limit\n3.Quit");
                    choice = in.nextLine();
                    switch (choice) {
                        case "1":
                            System.out.println("Enter commission (x > 0)");
                            bank.changeCommissionCredit(in.nextDouble());
                            break;
                        case "2":
                            System.out.println("Enter lower limit of a balance (x < 0 ");
                            bank.changeLowerLimitCredit(in.nextDouble());
                            break;
                        case "3":
                            break;
                        default:
                            System.out.println("Unexpected token\n");
                            break;
                    }

                    break;
                case "4":
                    return;
                default:
                    System.out.println("Unexpected token\n");
                    break;
            }
        }
    }
}