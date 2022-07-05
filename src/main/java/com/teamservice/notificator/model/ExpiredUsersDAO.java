package com.teamservice.notificator.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class ExpiredUsersDAO {

    @XmlElement(name = "ownerId")
    long owner;
    @XmlElement(name = "expiredUsers")
    List<UserDAO> userDAOArray;


    public ExpiredUsersDAO() {
    }

    public List<UserDAO> getUserArray() {
        return userDAOArray;
    }

    public void setUserArray(List<UserDAO> userDAOArray) {
        this.userDAOArray = userDAOArray;
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
        for (UserDAO userDAO : userDAOArray) {
            sb.append(userDAO).append(" ");
        }
        sb.append("\n").append(owner).append("\n");
        return sb.toString();
    }
}
