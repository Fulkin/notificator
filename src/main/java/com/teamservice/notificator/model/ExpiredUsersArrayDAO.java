package com.teamservice.notificator.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class ExpiredUsersArrayDAO {
    @XmlElement(name = "expiredUsersArr")
    List<ExpiredUsersDAO> userDAOArray;
}
