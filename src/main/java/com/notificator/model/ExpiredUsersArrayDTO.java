package com.notificator.model;

import jakarta.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import java.util.List;

/**
 * DTO for multiple {@code ExpiredUsers} entities. 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ExpiredUsersArr")
public class ExpiredUsersArrayDTO {

    @XmlSchemaType(name = "expiredUsersWithOwner")
    private List<ExpiredUsersDTO> expiredUsersWithOwner;

    public ExpiredUsersArrayDTO() {
    }

    public List<ExpiredUsersDTO> getExpiredUsersWithOwner() {
        return expiredUsersWithOwner;
    }

    public void setExpiredUsersWithOwner(List<ExpiredUsersDTO> expiredUsersWithOwner) {
        this.expiredUsersWithOwner = expiredUsersWithOwner;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Users: ");
        for (ExpiredUsersDTO userDAO : expiredUsersWithOwner) {
            sb.append(userDAO).append(" ");
        }
        return sb.toString();
    }
}