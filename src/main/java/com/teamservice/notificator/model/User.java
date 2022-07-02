package com.teamservice.notificator.model;

import jakarta.xml.bind.annotation.*;

import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    @XmlElement(name = "telegramId")
    private long telegramId;
    @XmlElement(name = "firstName")
    private String firstName;
    @XmlElement(name = "lastName")
    private String lastName;
    @XmlElement(name = "lastTrack")
    private Date lastTrack;

    public User() {
    }

    public User(long telegramId, String firstName, String lastName, Date lastTrack) {
        this.telegramId = telegramId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastTrack = lastTrack;
    }

    public long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(long telegramId) {
        this.telegramId = telegramId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getLastTrack() {
        return lastTrack;
    }

    public void setLastTrack(Date lastTrack) {
        this.lastTrack = lastTrack;
    }

    @Override
    public String toString() {
        return "User{" +
                "telegramId=" + telegramId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", lastTrack=" + lastTrack +
                '}';
    }
}
