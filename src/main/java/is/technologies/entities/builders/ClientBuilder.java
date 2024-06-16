package is.technologies.entities.builders;

import is.technologies.entities.Client;
import is.technologies.exceptions.ClientException;

public class ClientBuilder
{
    private String _firstName;
    private String _surname;
    private String _address;
    private int _passportId;

    public ClientBuilder(String firstName, String surname) throws ClientException {
        if (firstName == null || surname == null || firstName.isBlank() || surname.isBlank()) {
            throw new ClientException("Incorrect client's name input.txt.");
        }

        _firstName = firstName;
        _surname = surname;
    }

    public Client create() throws ClientException {
        var client = new Client(_firstName, _surname);
        if (_address != null)
        {
            client.enterOrChangeAddress(_address);
        }

        if (_passportId != 0)
        {
            client.enterOrChangePassport(_passportId);
        }

        reset();
        return client;
    }

    public ClientBuilder setAddress(String address) throws ClientException {
        if (address == null || address.isBlank()) {
            throw new ClientException("Incorrect client's address input.txt.");
        }

        _address = address;
        return this;
    }

    public ClientBuilder setPassport(int passportId) throws ClientException {
        if (passportId == 0)
        {
            throw new ClientException("Incorrect passport id input.txt.");
        }

        _passportId = passportId;
        return this;
    }

    private void reset()
    {
        _firstName = null;
        _surname = null;
        _address = null;
        _passportId = 0;
    }
}
