package com.notificator.service;

import com.notificator.model.ExpiredUsersArrayDTO;
import com.notificator.model.ExpiredUsersDTO;
import com.notificator.util.PropertiesUtil;
import com.notificator.util.SoapUtil;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import org.slf4j.Logger;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Service layer to get expired users from SOAP message and creating SOAP message
 * for further sending
 */
public class NotificatorService {
    private static final Logger log = getLogger(NotificatorService.class);
    private static SOAPConnectionFactory soapConnectionFactory;
    private static String teamSoapEndpointUrl;
    private static String routerSoapEndpointUrl;

    /**
     * Set uri for SOAP messages and creating SOAPConnectionFactory
     */
    public NotificatorService() {
        routerSoapEndpointUrl = PropertiesUtil.getProperty("router.url");
        teamSoapEndpointUrl = PropertiesUtil.getProperty("team.url");
        try {
            soapConnectionFactory = SOAPConnectionFactory.newInstance();
        } catch (SOAPException e) {
            log.error("SOAP create factory error", e);
        }
    }

    /**
     * Get expired users from SOAP message from a given uri
     *
     * @param teamUrl - given url for getting users
     * @return - group of users with their teamleader (or lector) to whom the response is sent
     */
    public ExpiredUsersArrayDTO getAllUsersFromTeam(String teamUrl) {
        ExpiredUsersArrayDTO expiredUsersArrayDTO = null;
        try (SOAPConnection soapConnection = soapConnectionFactory.createConnection()) {
            SOAPMessage getSoapResponse = soapConnection.call(
                    SoapUtil.createSOAPRequest(teamUrl, null),
                    teamSoapEndpointUrl);
            expiredUsersArrayDTO = SoapUtil.parserToUserArray(getSoapResponse);
        } catch (Exception e) {
            log.error("REQUEST SOAP Error!", e);
            return null;
        }
        return expiredUsersArrayDTO;
    }

    /**
     * Sending users for a given url
     *
     * @param expiredUsers - group of users with their teamleader (or lector) to whom the response is sent
     * @param routerUrl    - given url for sending users
     */
    public void setUsersToRouter(ExpiredUsersArrayDTO expiredUsers, String routerUrl) {
        try (SOAPConnection soapConnection = soapConnectionFactory.createConnection()) {
            List<ExpiredUsersDTO> listUsers = expiredUsers.getExpiredUsersWithOwner();
            for (ExpiredUsersDTO listUser : listUsers) {
                soapConnection.call(
                        SoapUtil.createSOAPRequest(routerUrl, listUser),
                        routerSoapEndpointUrl);
            }
        } catch (Exception e) {
            log.error("RESPONSE SOAP Error!", e);
        }
    }
}
