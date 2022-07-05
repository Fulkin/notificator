package com.teamservice.notificator.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ExpiredUsersArr")
public class Users {

    @XmlElement(name = "ownerId")
    long owner;
    @XmlElement(name = "expiredUsers")
    List<User> userArray;


    public Users() {
    }

    public List<User> getUserArray() {
        return userArray;
    }

    public void setUserArray(List<User> userArray) {
        this.userArray = userArray;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Users: ");
        for (User user : userArray) {
            sb.append(user).append(" ");
        }
        sb.append("\n").append(owner).append("\n");
        return sb.toString();
    }
}
