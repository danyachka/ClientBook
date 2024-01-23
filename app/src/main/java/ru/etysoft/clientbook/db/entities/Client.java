package ru.etysoft.clientbook.db.entities;

public class Client {

    private String id;

    private String name;

    private String telephoneNumber;

    public Client(String id, String name, String telephoneNumber) {
        this.id = id;
        this.name = name;
        this.telephoneNumber = telephoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
