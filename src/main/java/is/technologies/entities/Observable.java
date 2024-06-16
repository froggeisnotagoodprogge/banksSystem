package is.technologies.entities;

import is.technologies.exceptions.BankException;

public interface Observable {
    void addSubscriber(Observer client) throws BankException;

    void Notify();

    void unsubscribeClient(Observer client) throws BankException;
}
