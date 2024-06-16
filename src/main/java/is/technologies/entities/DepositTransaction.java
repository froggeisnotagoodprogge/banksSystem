package is.technologies.entities;

import is.technologies.exceptions.TransactionException;

public class DepositTransaction extends Transaction {
    public DepositTransaction(Account account, double amount) throws TransactionException {
        super(account, amount);
    }

    @Override
    void Execute() {
        account.depositMoney(amount);
        transactionSuccess = true;
        account.transactions.add(this);
    }

    @Override
    void UndoTransaction() {
        transactionCanceled = account.tryWithdrawMoney(amount);
    }
}
