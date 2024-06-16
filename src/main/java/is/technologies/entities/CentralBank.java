package is.technologies.entities;

import is.technologies.exceptions.AccountException;
import is.technologies.exceptions.CentralBankException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CentralBank implements Handler {
    private static CentralBank instance;
    private ArrayList<Bank> banks;

    private ArrayList<Client> clients;

    private ArrayList<Account> accounts;
    private ArrayList<Transaction> transactions;

    private CentralBank() {
        banks = new ArrayList<Bank>();
        clients = new ArrayList<Client>();
        accounts = new ArrayList<Account>();
        transactions = new ArrayList<Transaction>();
    }

    public static CentralBank getInstance() {
        if (instance == null) {
            instance = new CentralBank();
        }
        return instance;
    }

    public List<Bank> getBanks() {
        return Collections.unmodifiableList(banks);
    }

    public List<Client> getClients() {
        return Collections.unmodifiableList(clients);
    }

    public LocalDateTime cbTime() {
        return TimeProvider.getInstance().getCbTime();
    }

    public void addBank(Bank bank) throws CentralBankException {
        if (bank == null) {
            throw new CentralBankException("Adding null input.txt bank to cb");
        }
        if (banks.stream().anyMatch(b -> b.getId() == bank.getId())) {
            throw new CentralBankException("Adding already registered bank to cb.");
        }
        bank.setId(banks.size() + 1);
        banks.add(bank);
    }

    public void addClient(Client client) throws CentralBankException {
        if (client == null) {
            throw new CentralBankException("Adding null input.txt client to cb.");
        }
        client.setId(clients.size() + 1);
        clients.add(client);
    }

    void addAccount(Account account) {
        account.setId(accounts.size() + 1);
        accounts.add(account);
    }

    @Override
    public boolean handleTransaction(Transaction transaction) throws CentralBankException {
        if (transaction.getId() != 0) {
            throw new CentralBankException("Transaction have already been executed.");
        }

        transaction.Execute();
        if (!transaction.transactionSuccess) return false;
        transactionsHistory(transaction);
        return true;
    }

    @Override
    public boolean undoTransaction(Transaction transaction) throws CentralBankException {
        if (transaction.getId() == 0) {
            throw new CentralBankException("Attempt to undo not executed transaction.");
        }

        transaction.UndoTransaction();
        return transaction.transactionCanceled;
    }

    // дальше сильная связка id и index-а в листе, так как нет операций удаления банка/клиента/аккаунта из базы
    public Bank findBank(int id) {
        if (id > banks.size())
            return null;
        return banks.get(id - 1);
    }

    public Client findClient(int id) throws CentralBankException {
        if (id <= 0) {
            throw new CentralBankException("Incorrect id input.txt in the find method cb.");
        }
        if (id > clients.size())
            return null;
        return clients.get(id - 1);
    }

    public Account findAccount(int id) throws CentralBankException {
        if (id < 0) {
            throw new CentralBankException("Incorrect id input.txt in the find method cb.");
        }
        if (id > accounts.size())
            return null;
        return accounts.get(id - 1);
    }

    public Transaction findTransaction(int id) throws CentralBankException {
        if (id < 0) {
            throw new CentralBankException("Incorrect id input.txt in the find method cb.");
        }
        if (id > transactions.size())
            return null;
        return transactions.get(id - 1);
    }

    public void update() throws AccountException {
        for (Account acc : accounts) {
            acc.update();
        }
    }

    void transactionsHistory(Transaction transaction) {
        transaction.SetId(transactions.size() + 1);
        transactions.add(transaction);
    }
}
