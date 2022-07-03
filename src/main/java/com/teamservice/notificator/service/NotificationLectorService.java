package com.teamservice.notificator.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class NotificationLectorService extends NotificatorService {

    public NotificationLectorService() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("properties\\connection.properties")) {
            Properties props = new Properties();
            props.load(is);

            //todo add lector properties
            getTeamSoapAction = props.getProperty("team.action.lector.uri");
            addRouterSoapAction = props.getProperty("router.action.lector.uri");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file");
        }
    }
}
