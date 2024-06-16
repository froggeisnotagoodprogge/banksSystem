package is.technologies.entities;

import is.technologies.exceptions.AccountException;

import java.time.LocalDateTime;

public class DepositAccount extends Account {

    private LocalDateTime accountConversionDate;
    private Account state;

    public DepositAccount(Client owner, Bank bank) throws AccountException {
        super(owner, bank);
        accountConversionDate = CentralBank.getInstance().cbTime().plusYears(1);
        state = new DepositAccountState(owner, bank);
    }

    @Override
    public double getBalance() {
        return state.balance;
    }

    @Override
    public boolean tryWithdrawMoney(double amount) {
        return state.tryWithdrawMoney(amount);
    }

    @Override
    void update() throws AccountException {
        state.update();

        if (CentralBank.getInstance().cbTime() == accountConversionDate) {
            changeState();
        }
    }

    void changeState() throws AccountException {
        if (CentralBank.getInstance().cbTime().isBefore(accountConversionDate)) {
            throw new AccountException("Invalid method change account state call.");
        }
        var newState = new DebitAccount(owner, bank);
        newState.balance = state.balance;
        newState.transactions = state.transactions;
        state = newState;
    }
}
