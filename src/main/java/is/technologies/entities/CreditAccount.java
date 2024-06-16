package is.technologies.entities;

import is.technologies.exceptions.AccountException;

public class CreditAccount extends Account {
    CreditAccount(Client owner, Bank bank) throws AccountException {
        super(owner, bank);
    }

    @Override
    public boolean tryWithdrawMoney(double amount) {
        if (balance - amount < bank.getCreditSettings().getLowerLimitCreditAccount() ||
                (!owner.reliable && bank.getLimitForUnreliableClients() < amount)) {
            return false;
        }

        balance -= amount;
        return true;
    }

    @Override
    void update() {
        if (balance < 0) {
            drippedForThatMonth += bank.getCreditSettings().getCommissionCreditAccount();
        }

        if (CentralBank.getInstance().cbTime().getDayOfMonth() != creationTime.getDayOfMonth()) return;
        balance -= drippedForThatMonth;
        drippedForThatMonth = 0;
    }
}
