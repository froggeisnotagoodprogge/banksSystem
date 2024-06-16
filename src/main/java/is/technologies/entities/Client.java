package is.technologies.entities;

import is.technologies.exceptions.ClientException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Client implements Observer {
    private String surname;
    private String firstName;
    private String address;
    private int passportId;
    boolean reliable;
    private int id;
    private ArrayList<String> notifications;

    public Client(String firstName, String surname) throws ClientException {
        if (firstName == null || surname == null || firstName.isBlank() || surname.isBlank()) {
            throw new ClientException("Incorrect client's name input.txt.");
        }

        this.firstName = firstName;
        this.surname = surname;
        notifications = new ArrayList<>();
        reliable = false;
    }

    public int getId() {
        return id;
    }

    public List<String> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }

    public void enterOrChangeAddress(String address) throws ClientException {
        if (address == null || address.isBlank()) {
            throw new ClientException("Incorrect client's address input.txt.");
        }
        this.address = address;
        reliable = true;
    }

    public void enterOrChangePassport(int passportId) throws ClientException {
        if (passportId == 0) {
            throw new ClientException("Incorrect client's passport id input.txt.");
        }

        this.passportId = passportId;
        reliable = true;
    }

    @Override
    public void update() {
        notifications.add("I got smth");
    }

    void setId(int id) {
        this.id = id;
    }
}
