import is.technologies.entities.*;
import is.technologies.entities.builders.BankBuilder;
import is.technologies.exceptions.*;
import org.junit.jupiter.api.Test;

public class lab1Test {
    @Test
    public void CheckDepositTransactionAndBalance() throws BankException, CentralBankException, AccountException, ClientException, TransactionException {
        BankBuilder bankBuilder = new BankBuilder("alf", 100000);
        bankBuilder.setDebitPercent(2);
        bankBuilder.setDepositPercent(2, 5, 10, 100000, 1000000);
        bankBuilder.setCreditSettings(1000, -100000);
        Bank bank = bankBuilder.create();
        CentralBank.getInstance().addBank(bank);
        Client client = new Client("pasha", "kuznetsov");
        CentralBank.getInstance().addClient(client);
        Account account = bank.OpenAccount(client, AccountTypes.Debit);
        DepositTransaction transaction = new DepositTransaction(account, 10000);
        bank.handleTransaction(transaction);
        assert account.getBalance() == 10000;
    }

    @Test
    public void CheckUndoTransaction() throws AccountException, CentralBankException, ClientException, BankException, TransactionException {
        BankBuilder bankBuilder = new BankBuilder("alf", 100000);
        bankBuilder.setDebitPercent(2);
        bankBuilder.setDepositPercent(2, 5, 10, 100000, 1000000);
        bankBuilder.setCreditSettings(1000, -100000);
        Bank bank = bankBuilder.create();
        CentralBank.getInstance().addBank(bank);
        Client client = new Client("pasha", "kuznetsov");
        CentralBank.getInstance().addClient(client);
        Account account = bank.OpenAccount(client, AccountTypes.Debit);
        DepositTransaction transaction = new DepositTransaction(account, 10000);
        bank.handleTransaction(transaction);
        CentralBank.getInstance().undoTransaction(transaction);
        assert account.getBalance() == 0;
    }

    @Test
    public void TransferCheck() throws BankException, CentralBankException, ClientException, AccountException, TransactionException {
        BankBuilder bankBuilder = new BankBuilder("alf", 100000);
        bankBuilder.setDebitPercent(2);
        bankBuilder.setDepositPercent(2, 5, 10, 100000, 1000000);
        bankBuilder.setCreditSettings(1000, -100000);
        Bank bank = bankBuilder.create();
        CentralBank.getInstance().addBank(bank);
        Client client = new Client("pasha", "kuznetsov");
        CentralBank.getInstance().addClient(client);
        Account account1 = bank.OpenAccount(client, AccountTypes.Debit);
        Account account2 = bank.OpenAccount(client, AccountTypes.Debit);
        DepositTransaction transaction = new DepositTransaction(account1, 10000);
        TransferTransaction transaction1 = new TransferTransaction(account1, account2, 10000);
        bank.handleTransaction(transaction);
        bank.handleTransaction(transaction1);
        assert account2.getBalance() == 10000;
    }

    @Test
    public void FastForward() throws BankException, CentralBankException, ClientException, AccountException, TransactionException {
        BankBuilder bankBuilder = new BankBuilder("alf", 100000);
        bankBuilder.setDebitPercent(2);
        bankBuilder.setDepositPercent(2, 5, 10, 100000, 1000000);
        bankBuilder.setCreditSettings(1000, -100000);
        Bank bank = bankBuilder.create();
        CentralBank.getInstance().addBank(bank);
        Client client = new Client("pasha", "kuznetsov");
        CentralBank.getInstance().addClient(client);
        Account account1 = bank.OpenAccount(client, AccountTypes.Debit);
        DepositTransaction transaction = new DepositTransaction(account1, 10000);
        bank.handleTransaction(transaction);
        TimeProvider.getInstance().fastForwardMonth();
        assert account1.getBalance() != 10000;
    }

    @Test
    public void SubscribersAndSettingsChange() throws CentralBankException, BankException, ClientException, AccountException {
        BankBuilder bankBuilder = new BankBuilder("alf", 100000);
        bankBuilder.setDebitPercent(2);
        bankBuilder.setDepositPercent(2, 5, 10, 100000, 1000000);
        bankBuilder.setCreditSettings(1000, -100000);
        Bank bank = bankBuilder.create();
        CentralBank.getInstance().addBank(bank);
        Client client = new Client("pasha", "kuznetsov");
        CentralBank.getInstance().addClient(client);
        Account account1 = bank.OpenAccount(client, AccountTypes.Debit);
        bank.addSubscriber(client);
        bank.changePercentDebit(3);
        assert !client.getNotifications().isEmpty();
    }
}
