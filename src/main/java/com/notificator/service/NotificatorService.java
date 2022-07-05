package com.notificator.service;

import com.notificator.model.ExpiredUsersArray;
import com.notificator.model.ExpiredUsersDAO;
import com.notificator.util.PropertiesUtil;
import com.notificator.util.SoapUtil;
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

    public NotificatorService() {
        routerSoapEndpointUrl = PropertiesUtil.getProperty("router.url");
        teamSoapEndpointUrl = PropertiesUtil.getProperty("team.url");
        try  {
            soapConnectionFactory = SOAPConnectionFactory.newInstance();
        } catch (SOAPException e) {
            throw new IllegalStateException("Invalid config file");
        }
    }

    public ExpiredUsersArray getAllUsersFromTeam(String teamUrl) {
        ExpiredUsersArray expiredUsersArray = null;
        try (SOAPConnection soapConnection = soapConnectionFactory.createConnection()) {

            SOAPMessage getSoapResponse = soapConnection.call(
                    SoapUtil.createSOAPRequest(teamUrl, null),
                    teamSoapEndpointUrl);
            expiredUsersArray = SoapUtil.parserToUserArray(getSoapResponse);
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
        }
        return expiredUsersArray;
    }

    public void setUsersToRouter(ExpiredUsersArray expiredUsers, String routerUrl) {
        try (SOAPConnection soapConnection = soapConnectionFactory.createConnection()) {

            List<ExpiredUsersDAO> listUsers = expiredUsers.getExpiredUsersWithOwner();
            for (ExpiredUsersDAO listUser : listUsers) {
                soapConnection.call(
                        SoapUtil.createSOAPRequest(routerUrl, listUser),
                        routerSoapEndpointUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
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
}
