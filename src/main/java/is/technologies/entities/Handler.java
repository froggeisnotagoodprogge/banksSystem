package is.technologies.entities;

import is.technologies.exceptions.BankException;
import is.technologies.exceptions.CentralBankException;

public interface Handler {
    boolean handleTransaction(Transaction transaction) throws BankException, CentralBankException;

    boolean undoTransaction(Transaction transaction) throws BankException, CentralBankException;
}
