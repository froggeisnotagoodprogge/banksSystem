package is.technologies.entities.banks.settings;

import is.technologies.exceptions.BankException;

public class DebitSettings {
    private double percentDebitAccount;

    public DebitSettings(double percent) throws BankException {
        if (percent < 0) {
            throw new BankException("Incorrect bank debit settings input.txt.");
        }

        percentDebitAccount = percent;
    }

    public double getPercentDebitAccount() {
        return percentDebitAccount;
    }

    public boolean changePercent(double percent) {
        if (percent < 0) return false;
        percentDebitAccount = percent;
        return true;
    }
}
