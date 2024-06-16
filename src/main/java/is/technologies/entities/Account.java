package is.technologies.entities;

import is.technologies.entities.Bank;
import is.technologies.entities.Client;
import is.technologies.entities.Transaction;
import is.technologies.exceptions.AccountException;

import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class Account {
    public Bank bank;
    Client owner;
    protected double balance;

    int id;

    ArrayList<Transaction> transactions;
    protected LocalDateTime creationTime;
    protected double drippedForThatMonth;
    public Account(Client owner, Bank bank) throws AccountException {
        if (owner == null || bank == null) {
            throw new AccountException("Null account input.txt.");
        }

        this.owner = owner;
        this.bank = bank;
        balance = 0;
        transactions = new ArrayList<>();
        creationTime = CentralBank.getInstance().cbTime();
        drippedForThatMonth = 0;
    }

    public int getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public abstract boolean tryWithdrawMoney(double amount);

    public void depositMoney(double amount) {
        balance += amount;
    }

    void setId(int id) {
        this.id = id;
    }

    abstract void update() throws AccountException;

}
