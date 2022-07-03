package com.teamservice.notificator.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ExpiredUsers")
public class Users {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Users: ");
        for (User user : userArray) {
            sb.append(user).append(" ");
        }
        return sb.toString();
    }
}
