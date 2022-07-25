package com.notificator.service;

import com.notificator.model.ExpiredUsersArrayDTO;
import com.notificator.model.ExpiredUsersDTO;
import com.notificator.util.RouterProperties;
import com.notificator.util.SoapUtil;
import com.notificator.util.TeamProperties;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Service layer to get expired users from SOAP message and creating SOAP message
 * for further sending
 */
@Service
public class NotificatorServiceImpl implements NotificatorService {
    private static final Logger log = getLogger(NotificatorServiceImpl.class);
    private static SOAPConnectionFactory soapConnectionFactory;

    @Autowired
    RouterProperties router;

    @Autowired
    TeamProperties team;

    /**
     * Set uri for SOAP messages and creating SOAPConnectionFactory
     */
    public NotificatorServiceImpl() {
        try {
            soapConnectionFactory = SOAPConnectionFactory.newInstance();
        } catch (SOAPException e) {
            log.error("SOAP create factory error", e);
        }
    }


    @Override
    public void getAndSendExpiredUsersForLector() {
        ExpiredUsersArrayDTO expUsers = getAllUsersFromTeam(team.getActionLectorUri());
        if (expUsers == null) {
            log.info("expired users is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        log.info("send request to router service with uri: {}", router.getActionLectorUri());
        sendUsersToRouter(expUsers, router.getActionLectorUri());
    }

    @Override
    public void getAndSendExpiredUsersForTeamLead() {
        ExpiredUsersArrayDTO expUsers = getAllUsersFromTeam(team.getActionTeamleadUri());
        if (expUsers == null) {
            log.info("expired users is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        log.info("send request to router service with uri: {}", team.getActionTeamleadUri());
        sendUsersToRouter(expUsers, team.getActionTeamleadUri());
    }

    /**
     * Get expired users from SOAP message from a given uri
     *
     * @param teamUrl - given url for getting users
     * @return - group of users with their teamleader (or lector) to whom the response is sent
     */
    private ExpiredUsersArrayDTO getAllUsersFromTeam(String teamUrl) {
        ExpiredUsersArrayDTO expiredUsersArrayDTO = null;
        try  {
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            SOAPMessage getSoapResponse = soapConnection.call(
                    SoapUtil.createSOAPRequest(teamUrl, null),
                    team.getUri());
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
    private void sendUsersToRouter(ExpiredUsersArrayDTO expiredUsers, String routerUrl) {
        try {
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            List<ExpiredUsersDTO> listUsers = expiredUsers.getExpiredUsersWithOwner();
            for (ExpiredUsersDTO listUser : listUsers) {
                soapConnection.call(
                        SoapUtil.createSOAPRequest(routerUrl, listUser),
                        router.getUri());
            }
        } catch (Exception e) {
            log.error("RESPONSE SOAP Error!", e);
        }
    }
}
