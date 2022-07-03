package com.teamservice.notificator.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class NotificationTeamLeadService extends NotificatorService {

    public NotificationTeamLeadService() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("properties\\connection.properties")) {
            Properties props = new Properties();
            props.load(is);

            getTeamSoapAction = props.getProperty("team.action.teamlead.uri");
            addRouterSoapAction = props.getProperty("router.action.teamlead.uri");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file");
        }
    }

}
