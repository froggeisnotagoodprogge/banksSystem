package is.technologies.entities;

import is.technologies.exceptions.AccountException;

public class DepositAccountState extends Account {
    public DepositAccountState(Client owner, Bank bank) throws AccountException {
        super(owner, bank);
    }

    @Override
    public boolean tryWithdrawMoney(double amount) {
        return false;
    }

    @Override
    void update() {
        // добавляем суточный процент, в переменной хранится процент за год
        double percent = bank.getDepositSettings().getLowerPercentDepositAccount();
        if (balance >= bank.getDepositSettings().getFirstLimitDepositAccount()) {
            percent = bank.getDepositSettings().getMiddlePercentDepositAccount();
            if (balance > bank.getDepositSettings().getSecondLimitDepositAccount()) {
                percent = bank.getDepositSettings().getHigherPercentDepositAccount();
            }
        }

        drippedForThatMonth += balance * percent / (CentralBank.getInstance().cbTime().toLocalDate().lengthOfYear() * 100);
        if (CentralBank.getInstance().cbTime().getDayOfMonth() != creationTime.getDayOfMonth()) return;
        balance += drippedForThatMonth;
        drippedForThatMonth = 0;
    }
}
