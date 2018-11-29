package com.jhongpananon.sqlite_project.Features.CreateAddress;

import java.io.Serializable;

public class Exercise implements Serializable {
    private long id;
    private String name;
    private long registrationNumber;
    private long date;
    private long set;
    private double weight;

    public Exercise(int id, String name, long registrationNumber, long date, long set, double weight) {
        this.id = id;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.date = date;
        this.set = set;
        this.weight = weight;
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

    public long getSet() { return set; }
    public void setSet(long set) { this.set = set; }

    public double getWeight() { return weight; }
    public void setWeight(long set) { this.weight = weight; }
}
