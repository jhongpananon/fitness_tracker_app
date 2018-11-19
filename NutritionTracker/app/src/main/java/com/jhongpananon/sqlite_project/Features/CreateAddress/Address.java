package com.jhongpananon.sqlite_project.Features.CreateAddress;

public class Address {
    private long id;
    private String name;
    private long registrationNumber;
    private long date;

    public Address(int id, String name, long registrationNumber, long date) {
        this.id = id;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(long registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
}
