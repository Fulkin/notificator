package com.teamservice.notificator.service;

import com.teamservice.notificator.model.User;
import com.teamservice.notificator.util.SoapUtil;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class NotificatorService {
    protected static SOAPConnectionFactory soapConnectionFactory;

    protected static String teamSoapEndpointUrl;
    protected static String routerSoapEndpointUrl;

    protected static String getTeamSoapAction;
    protected static String addRouterSoapAction;

    protected NotificatorService() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("properties\\connection.properties")) {
            soapConnectionFactory = SOAPConnectionFactory.newInstance();
            Properties props = new Properties();
            props.load(is);
            routerSoapEndpointUrl = props.getProperty("router.url");
            teamSoapEndpointUrl = props.getProperty("team.url");
        } catch (IOException | SOAPException e) {
            throw new IllegalStateException("Invalid config file");
        }
    }

    public List<User> getAllUsersFromTeam() {
        List<User> userList = null;
        try (SOAPConnection soapConnection = soapConnectionFactory.createConnection()) {

            SOAPMessage getSoapResponse = soapConnection.call(
                    SoapUtil.createSOAPRequest(getTeamSoapAction, null),
                    teamSoapEndpointUrl);
            userList = SoapUtil.parserToUserArray(getSoapResponse);
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
        }
        return userList;
    }

    public void setUsersToRouter(List<User> array) {
        try (SOAPConnection soapConnection = soapConnectionFactory.createConnection()) {

            SOAPMessage addSoapMessage = soapConnection.call(
                    SoapUtil.createSOAPRequest(addRouterSoapAction, array),
                    routerSoapEndpointUrl);
            List<User> array1 = SoapUtil.parserToUserArray(addSoapMessage);
            System.out.println("Response SOAP Message:");
            addSoapMessage.writeTo(System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
