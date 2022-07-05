package com.notificator.model;

import jakarta.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ExpiredUsersArr")
public class ExpiredUsersArray {

    @XmlSchemaType(name = "expiredUsersWithOwner")
    private List<ExpiredUsersDAO> expiredUsersWithOwner;

    public ExpiredUsersArray() {
    }

    public List<ExpiredUsersDAO> getExpiredUsersWithOwner() {
        return expiredUsersWithOwner;
    }

    public void setExpiredUsersWithOwner(List<ExpiredUsersDAO> expiredUsersWithOwner) {
        this.expiredUsersWithOwner = expiredUsersWithOwner;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Users: ");
        for (ExpiredUsersDAO userDAO : expiredUsersWithOwner) {
            sb.append(userDAO).append(" ");
        }
        return sb.toString();
    }
}