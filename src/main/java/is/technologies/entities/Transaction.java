package is.technologies.entities;

import is.technologies.exceptions.TransactionException;

public abstract class Transaction {

    Account account;
    double amount;

    int id;

    boolean transactionSuccess;
    boolean transactionCanceled;
    boolean transactionBetweenBanks;

    abstract void Execute();

    abstract void UndoTransaction();

    public Account getAccount() {
        return account;
    }

    public int getId() {
        return id;
    }

    boolean isTransactionSuccess() {
        return transactionSuccess;
    }

    boolean isTransactionCanceled() {
        return transactionCanceled;
    }

    boolean isTransactionBetweenBanks() {
        return transactionBetweenBanks;
    }

    Transaction(Account account, double amount) throws TransactionException {
        if (amount < 0) {
            throw new TransactionException("Incorrect transaction amount input.txt value.");
        }

        if (account.id == 0) {
            throw new TransactionException("Incorrect transaction account input.txt value.");
        }

        this.account = account;
        this.amount = amount;
        this.transactionSuccess = false;
        this.transactionCanceled = false;
        this.transactionBetweenBanks = false;
    }

    void SetId(int id) {
        this.id = id;
    }
}
