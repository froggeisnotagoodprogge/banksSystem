package is.technologies.entities;

import is.technologies.exceptions.AccountException;

public class DebitAccount extends Account {
    DebitAccount(Client owner, Bank bank) throws AccountException {
        super(owner, bank);
    }

    @Override
    public boolean tryWithdrawMoney(double amount) {
        if (balance < amount || (!owner.reliable && bank.getLimitForUnreliableClients() < amount)) {
            return false;
        }

        balance -= amount;
        return true;
    }

    @Override
    public void depositMoney(double amount) {
        balance += amount;
    }

    @Override
    void update() {
        // добавляем суточный процент, в переменной хранится процент за год
        drippedForThatMonth += balance * bank.getDebitSettings().getPercentDebitAccount() / (365 * 100);
        if (CentralBank.getInstance().cbTime().getDayOfMonth() != creationTime.getDayOfMonth()) return;
        balance += drippedForThatMonth;
        drippedForThatMonth = 0;
    }
}
