package is.technologies.entities;

import is.technologies.exceptions.TransactionException;

public class WithdrawTransaction extends Transaction {
    public WithdrawTransaction(Account account, double amount) throws TransactionException {
        super(account, amount);
    }

    @Override
    void Execute() {
        transactionSuccess = account.tryWithdrawMoney(amount);
        if (transactionSuccess) {
            account.transactions.add(this);
        }
    }

    @Override
    void UndoTransaction() {
        account.depositMoney(amount);
        transactionCanceled = true;
    }
}
