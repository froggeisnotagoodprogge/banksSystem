package is.technologies.entities;

import is.technologies.entities.banks.settings.CreditSettings;
import is.technologies.entities.banks.settings.DebitSettings;
import is.technologies.entities.banks.settings.DepositSettings;
import is.technologies.exceptions.AccountException;
import is.technologies.exceptions.BankException;
import is.technologies.exceptions.CentralBankException;

import java.util.ArrayList;
import java.util.List;

import static is.technologies.entities.AccountTypes.Credit;

public class Bank implements Observable, Handler {
    private String name;
    private int id;
    private List<Account> accounts;
    private double limitForUnreliableClients;
    private DebitSettings debitSettings;
    private DepositSettings depositSettings;
    private CreditSettings creditSettings;

    private List<Observer> subscribers;

    public Bank(String name, double limitForUnreliableClients) throws BankException {
        if (name == null || name.isBlank()) {
            throw new BankException("Incorrect bank name.");
        }
        if (limitForUnreliableClients < 0) {
            throw new BankException("Incorrect bank limit input.txt value.");
        }

        this.name = name;
        this.limitForUnreliableClients = limitForUnreliableClients;
        accounts = new ArrayList<>();
        subscribers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public double getLimitForUnreliableClients() {
        return limitForUnreliableClients;
    }

    public DebitSettings getDebitSettings() {
        return debitSettings;
    }

    public DepositSettings getDepositSettings() {
        return depositSettings;
    }

    public CreditSettings getCreditSettings() {
        return creditSettings;
    }

    public void setDebitPercent(double percent) throws BankException {
        if (debitSettings != null) {
            throw new BankException("Attempt to reset debit settings.");
        }
        debitSettings = new DebitSettings(percent);
    }

    public void setDepositPercent(double lowerPercent,
                                  double middlePercent,
                                  double higherPercent,
                                  double firstLimit,
                                  double secondLimit) throws BankException {
        if (depositSettings != null) {
            throw new BankException("Attempt to reset deposit settings.");
        }
        depositSettings = new DepositSettings(lowerPercent, middlePercent, higherPercent, firstLimit, secondLimit);
    }

    public void setCreditSettings(double commission, double lowerLimit) throws BankException {
        if (creditSettings != null) {
            throw new BankException("Attempt to reset credit settings.");
        }
        creditSettings = new CreditSettings(commission, lowerLimit);
    }

    @Override
    public void addSubscriber(Observer client) throws BankException {
        if (accounts.stream().allMatch(a -> a.owner.getId() != client.getId())) {
            throw new BankException("Attempt to add a subscriber, who don't have an account in that bank.");
        }

        subscribers.add(client);
    }

    @Override
    public void unsubscribeClient(Observer client) throws BankException {
        if (subscribers.stream().allMatch(s -> s.getId() != client.getId())) {
            throw new BankException("Attempt to unsubscribe client, who is not subscribed.");
        }

        //todo doesn't work
        //subscribers.remove(subscribers.stream().findAny(s -> s.getId() == client.getId()));
    }

    public boolean changeLimitForUnreliableClients(double amount) {
        if (amount < 0) return false;
        limitForUnreliableClients = amount;
        Notify();
        return true;
    }

    public boolean changePercentDebit(double percent) {
        if (debitSettings == null || !debitSettings.changePercent(percent)) return false;
        Notify();
        return true;
    }

    public boolean changeLowerPercentDeposit(double percent) {
        if (depositSettings == null || !depositSettings.changeLowerPercent(percent)) return false;
        Notify();
        return true;
    }

    public boolean changeMiddlePercentDeposit(double percent) {
        if (depositSettings == null || !depositSettings.changeMiddlePercent(percent)) return false;
        Notify();
        return true;
    }

    public boolean changeHigherPercentDeposit(double percent) {
        if (depositSettings == null || !depositSettings.changeHigherPercent(percent)) return false;
        Notify();
        return true;
    }

    public boolean changeFirstLimitDeposit(double amount) {
        if (depositSettings == null || !depositSettings.changeFirstLimit(amount)) return false;
        Notify();
        return true;
    }

    public boolean changeSecondLimitDeposit(double amount) {
        if (depositSettings == null || !depositSettings.changeSecondLimit(amount)) return false;
        Notify();
        return true;
    }

    public boolean changeCommissionCredit(double amount) {
        if (creditSettings == null || !creditSettings.changeCommissionCredit(amount)) return false;
        Notify();
        return true;
    }

    public boolean changeLowerLimitCredit(double amount) {
        if (creditSettings == null || !creditSettings.changeLowerLimitCredit(amount)) return false;
        Notify();
        return true;
    }

    @Override
    public void Notify() {
        for (var sub : subscribers) {
            sub.update();
        }
    }

    @Override
    public boolean handleTransaction(Transaction transaction) throws BankException, CentralBankException {
        if (transaction.getId() != 0) {
            throw new BankException("Transaction have already been executed.");
        }

        if (!transaction.transactionBetweenBanks) {
            transaction.Execute();
        } else {
            CentralBank.getInstance().handleTransaction(transaction);
        }
        if (!transaction.transactionSuccess) return false;
        CentralBank.getInstance().transactionsHistory(transaction);
        return true;
    }

    @Override
    public boolean undoTransaction(Transaction transaction) throws BankException, CentralBankException {
        if (transaction.getId() == 0) {
            throw new BankException("Attempt to undo not executed transaction.");
        }

        if (!transaction.transactionBetweenBanks) {
            transaction.UndoTransaction();
        } else {
            CentralBank.getInstance().undoTransaction(transaction);
        }

        return transaction.transactionCanceled;
    }

    public Account OpenAccount(Client client, AccountTypes type) throws AccountException {
        if (client == null || client.getId() == 0 )
        {
            throw new AccountException("Incorrect account input.");
        }

        if (type == AccountTypes.Debit && debitSettings != null)
        {
            var account = new DebitAccount(client, this);
            accounts.add(account);
            CentralBank.getInstance().addAccount(account);
            return account;
        }
        else if (type == AccountTypes.Deposit && depositSettings != null)
        {
            var account = new DepositAccount(client, this);
            accounts.add(account);
            CentralBank.getInstance().addAccount(account);
            return account;
        }
        else if (type == AccountTypes.Credit && (creditSettings != null))
        {
            var account = new CreditAccount(client, this);
            accounts.add(account);
            CentralBank.getInstance().addAccount(account);
            return account;
        }
        else
        {
            throw new AccountException("Incorrect account input.");
        }
    }

    void setId(int id) {
        this.id = id;
    }
}
