package com.teamservice.notificator.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "userArray")
public class Users {
    @XmlElement(name = "item")
    User[] userArray;

    public Users() {
    }

    public User[] getUserArray() {
        return userArray;
    }

    public void setUserArray(User[] userArray) {
        this.userArray = userArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Users: ");
        for (int i = 0; i < userArray.length; i++) {
            sb.append(userArray[0]).append(" ");
        }
        return sb.toString();
    }
}
