package com.teamservice.notificator.service;

import com.teamservice.notificator.model.User;
import com.teamservice.notificator.util.PropertiesUtil;
import com.teamservice.notificator.util.SoapUtil;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;

import java.nio.file.Path;
import java.util.List;

public class NotificatorService {
    protected static SOAPConnectionFactory soapConnectionFactory;

    protected static String teamSoapEndpointUrl;
    protected static String routerSoapEndpointUrl;

    protected static String getTeamSoapAction;
    protected static String addRouterSoapAction;

    protected NotificatorService() {
        routerSoapEndpointUrl = PropertiesUtil.getProperty("router.url");
        teamSoapEndpointUrl = PropertiesUtil.getProperty("team.url");
        try  {
            soapConnectionFactory = SOAPConnectionFactory.newInstance();
        } catch (SOAPException e) {
            throw new IllegalStateException("Invalid config file");
        }
    }

    public void setFile(Path path) {
        try (SOAPConnection soapConnection = soapConnectionFactory.createConnection()) {
            SOAPMessage call = soapConnection.call(
                    SoapUtil.createByteSOAPRequest(path),
                    "http://localhost:8080/router/soap/files");
            System.out.println("\n\n");
            call.writeTo(System.out);
            System.out.println("\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsersFromTeam() {
        List<User> userList = null;
        try (SOAPConnection soapConnection = soapConnectionFactory.createConnection()) {

            SOAPMessage getSoapResponse = soapConnection.call(
                    SoapUtil.createSOAPRequest(getTeamSoapAction, null),
                    teamSoapEndpointUrl);
            getSoapResponse.writeTo(System.out);
            System.out.println();
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
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
