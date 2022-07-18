package com.notificator.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * User DTO, who didn't mark the completion of the track
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "expiredUsers")
public class UserDTO {

    @XmlElement(name = "telegramId")
    private long telegramId;
    @XmlElement(name = "firstName")
    private String firstName;
    @XmlElement(name = "lastName")
    private String lastName;
    /**
     * Date of the last mark of the track
     */
    @XmlElement(name = "lastModified")
    private String lastModified;

    public UserDTO() {
    }

    public UserDTO(long telegramId, String firstName, String lastName, String lastModified) {
        this.telegramId = telegramId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastModified = lastModified;
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

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "UserDAO{" +
                "telegramId=" + telegramId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", lastModified='" + lastModified + '\'' +
                '}';
    }
}
