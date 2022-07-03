package com.teamservice.notificator.service;

import com.teamservice.notificator.model.User;
import com.teamservice.notificator.model.Users;
import com.teamservice.notificator.util.SoapUtil;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.*;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
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
