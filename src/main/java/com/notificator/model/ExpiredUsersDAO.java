package com.notificator.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "expiredUsersWithOwner")
public class ExpiredUsersDAO {

    @XmlElement(name = "ownerId")
    long ownerId;
    @XmlElement(name = "expiredUsers")
    List<UserDAO> expiredUsers;


    public ExpiredUsersDAO() {
    }

    public List<UserDAO> getExpiredUsers() {
        return expiredUsers;
    }

    public void setExpiredUsers(List<UserDAO> expiredUsers) {
        this.expiredUsers = expiredUsers;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Users: ");
        for (UserDAO userDAO : expiredUsers) {
            sb.append(userDAO).append(" ");
        }
        sb.append("\nownerID: ").append(ownerId).append("\n");
        return sb.toString();
    }
}
