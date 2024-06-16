package is.technologies.entities;

import is.technologies.exceptions.TransactionException;

public class TransferTransaction extends Transaction {
    private Account receiver;

    public TransferTransaction(Account sender, Account receiver, double amount) throws TransactionException {
        super(sender, amount);
        if (receiver.id == 0) {
            throw new TransactionException("Incorrect transaction account input.txt value.");
        }
        this.receiver = receiver;
        if (sender.bank.getId() == receiver.bank.getId()) {
            transactionBetweenBanks = true;
        }
    }

    @Override
    void Execute() {
        transactionSuccess = account.tryWithdrawMoney(amount);
        if (transactionSuccess) {
            receiver.depositMoney(amount);
            account.transactions.add(this);
            receiver.transactions.add(this);
        }
    }

    @Override
    void UndoTransaction() {
        transactionCanceled =
                receiver.tryWithdrawMoney(amount);
        if (transactionCanceled) {
            account.depositMoney(amount);
        }
    }
}
