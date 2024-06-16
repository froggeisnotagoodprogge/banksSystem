package is.technologies.entities.builders;

import is.technologies.entities.Bank;
import is.technologies.exceptions.BankException;

public class BankBuilder {
    private String name;
    private double limitForUnreliableClients;
    private double percentDebitAccount;
    private double lowerPercentDepositAccount;
    private double middlePercentDepositAccount;
    private double higherPercentDepositAccount;
    private double firstLimitDepositAccount;
    private double secondLimitDepositAccount;
    private double commissionCreditAccount;
    private double lowerLimitCreditAccount;

    public BankBuilder(String name, double limitForUnreliableClients) throws BankException {
        if (name == null || name.isEmpty()) {
            throw new BankException("Incorrect bank name.");
        }

        if (limitForUnreliableClients < 0) {
            throw new BankException("Incorrect bank limit input.txt value.");
        }
        this.name = name;
        this.limitForUnreliableClients = limitForUnreliableClients;
    }

    public Bank create() throws BankException {
        var bank = new Bank(name, limitForUnreliableClients);
        if (percentDebitAccount != 0) {
            bank.setDebitPercent(percentDebitAccount);
        }

        if (lowerPercentDepositAccount != 0 && middlePercentDepositAccount != 0 && higherPercentDepositAccount != 0
                && firstLimitDepositAccount != 0 && secondLimitDepositAccount != 0) {
            bank.setDepositPercent(
                    lowerPercentDepositAccount,
                    middlePercentDepositAccount,
                    higherPercentDepositAccount,
                    firstLimitDepositAccount,
                    secondLimitDepositAccount);
        }

        if (commissionCreditAccount != 0 && lowerLimitCreditAccount != 0) {
            bank.setCreditSettings(commissionCreditAccount, lowerLimitCreditAccount);
        }

        reset();
        return bank;
    }

    public void setDebitPercent(double percent) throws BankException {
        if (percent < 0) {
            throw new BankException("Incorrect bank debit settings input.txt.");
        }

        percentDebitAccount = percent;
    }

    public void setDepositPercent(
            double lowerPercent,
            double middlePercent,
            double higherPercent,
            double firstLimit,
            double secondLimit) throws BankException {
        if (lowerPercent < 0 || middlePercent < 0 || higherPercent < 0 || firstLimit >= secondLimit) {
            throw new BankException("Incorrect bank deposit settings input.txt.");
        }

        lowerPercentDepositAccount = lowerPercent;
        middlePercentDepositAccount = middlePercent;
        higherPercentDepositAccount = higherPercent;
        firstLimitDepositAccount = firstLimit;
        secondLimitDepositAccount = secondLimit;
    }

    public void setCreditSettings(double commission, double lowerLimit) throws BankException {
        if (commission < 0 || lowerLimit >= 0) {
            throw new BankException("Incorrect bank credit settings input.txt.");
        }

        commissionCreditAccount = commission;
        lowerLimitCreditAccount = lowerLimit;
    }

    private void reset() {
        name = null;
        percentDebitAccount = 0;
        lowerPercentDepositAccount = 0;
        middlePercentDepositAccount = 0;
        higherPercentDepositAccount = 0;
        firstLimitDepositAccount = 0;
        secondLimitDepositAccount = 0;
        commissionCreditAccount = 0;
        lowerLimitCreditAccount = 0;
    }
}
