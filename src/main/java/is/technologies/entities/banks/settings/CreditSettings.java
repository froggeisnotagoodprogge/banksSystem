package is.technologies.entities.banks.settings;

import is.technologies.exceptions.BankException;

public class CreditSettings {
    private double commissionCreditAccount;
    private double lowerLimitCreditAccount;

    public CreditSettings(double commission, double lowerLimit) throws BankException {
        if (commission < 0 || lowerLimit >= 0) {
            throw new BankException("Incorrect bank credit settings input.txt.");
        }

        commissionCreditAccount = commission;
        lowerLimitCreditAccount = lowerLimit;
    }

    public double getCommissionCreditAccount() {
        return commissionCreditAccount;
    }

    public double getLowerLimitCreditAccount() {
        return lowerLimitCreditAccount;
    }

    public boolean changeCommissionCredit(double amount) {
        if (amount < 0) return false;
        commissionCreditAccount = amount;
        return true;
    }

    public boolean changeLowerLimitCredit(double amount) {
        if (amount >= 0) return false;
        lowerLimitCreditAccount = amount;
        return true;
    }
}
