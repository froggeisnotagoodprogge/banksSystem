package is.technologies.entities.banks.settings;

import is.technologies.exceptions.BankException;

public class DepositSettings {
    private double lowerPercentDepositAccount;

    private double middlePercentDepositAccount;
    private double higherPercentDepositAccount;
    private double firstLimitDepositAccount;
    private double secondLimitDepositAccount;
    public DepositSettings(
            double lowerPercent,
            double middlePercent,
            double higherPercent,
            double firstLimit,
            double secondLimit) throws BankException {
        if (lowerPercent < 0 || middlePercent < 0 || higherPercent < 0 || firstLimit >= secondLimit)
        {
            throw new BankException("Incorrect bank deposit settings input.txt.");
        }

        lowerPercentDepositAccount = lowerPercent;
        middlePercentDepositAccount = middlePercent;
        higherPercentDepositAccount = higherPercent;
        firstLimitDepositAccount = firstLimit;
        secondLimitDepositAccount = secondLimit;
    }
    public double getLowerPercentDepositAccount() {
        return lowerPercentDepositAccount;
    }

    public double getMiddlePercentDepositAccount() {
        return middlePercentDepositAccount;
    }

    public double getHigherPercentDepositAccount() {
        return higherPercentDepositAccount;
    }

    public double getFirstLimitDepositAccount() {
        return firstLimitDepositAccount;
    }

    public double getSecondLimitDepositAccount() {
        return secondLimitDepositAccount;
    }
    public boolean changeLowerPercent(double percent)
    {
        if (percent < 0 || percent > middlePercentDepositAccount) return false;
        lowerPercentDepositAccount = percent;
        return true;
    }

     public boolean changeMiddlePercent(double percent)
    {
        if (percent < 0 || percent > higherPercentDepositAccount || percent < lowerPercentDepositAccount) return false;
        middlePercentDepositAccount = percent;
        return true;
    }

     public boolean changeHigherPercent(double percent)
    {
        if (percent < 0 || percent < middlePercentDepositAccount) return false;
        higherPercentDepositAccount = percent;
        return true;
    }

     public boolean changeFirstLimit(double amount)
    {
        if (amount < 0 || amount > secondLimitDepositAccount) return false;
        firstLimitDepositAccount = amount;
        return true;
    }

    public boolean changeSecondLimit(double amount)
    {
        if (amount < 0 || amount < firstLimitDepositAccount) return false;
        secondLimitDepositAccount = amount;
        return true;
    }
}
