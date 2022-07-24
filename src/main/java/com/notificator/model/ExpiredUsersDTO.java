package com.notificator.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * DTO for group users with their teamleader.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "expiredUsersWithOwner")
public class ExpiredUsersDTO {

    /**
     * Leader telegram id
     */
    @XmlElement(name = "ownerId")
    long ownerId;

    /**
     * list of users, who didn't mark the completion of the track
     */
    @XmlElement(name = "expiredUsers")
    List<UserDTO> expiredUsers;


    public ExpiredUsersDTO() {
    }

    public List<UserDTO> getExpiredUsers() {
        return expiredUsers;
    }

    public void setExpiredUsers(List<UserDTO> expiredUsers) {
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
        for (UserDTO userDTO : expiredUsers) {
            sb.append(userDTO).append(" ");
        }
        sb.append("\nownerID: ").append(ownerId).append("\n");
        return sb.toString();
    }
}
