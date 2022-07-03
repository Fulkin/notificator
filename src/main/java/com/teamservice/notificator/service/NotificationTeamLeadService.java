package com.teamservice.notificator.service;

import com.teamservice.notificator.model.User;
import com.teamservice.notificator.util.SoapUtil;
import jakarta.xml.soap.*;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;


public class NotificationTeamLeadService {

    private static SOAPConnectionFactory soapConnectionFactory;

    private static DateFormat dateToSoap = new SimpleDateFormat("yyyy-dd-MM'T'hh:mm:ss");
    private static String spaceTeamUri;
    private static String spaceRouterUri;

    private static String teamSoapEndpointUrl;
    private static String routerSoapEndpointUrl;

    private static String teamService;
    private static String routerService;

    private static String methodTeam;
    private static String methodRouter;

    private static String getTeamSoapAction;
    private static String addRouterSoapAction;

    public NotificationTeamLeadService() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("properties\\connection.properties")) {
            soapConnectionFactory = SOAPConnectionFactory.newInstance();
            Properties props = new Properties();
            props.load(is);
            teamService = props.getProperty("team.notificator.service");
            routerService = props.getProperty("router.notificator.service");

            routerSoapEndpointUrl = props.getProperty("router.url");
            teamSoapEndpointUrl = props.getProperty("team.url");

            spaceTeamUri = props.getProperty("team.space.uri");
            spaceRouterUri = props.getProperty("router.space.uri");

            methodTeam = props.getProperty("team.teamlead.notificator.soapaction");
            methodRouter = props.getProperty("router.teamlead.notificator.soapaction");

            getTeamSoapAction = spaceTeamUri + "/" + teamService + "/" + methodTeam + "Response";
            addRouterSoapAction = spaceTeamUri + "/" + routerService + "/" + methodRouter + "Response";
        } catch (IOException | SOAPException e) {
            throw new IllegalStateException("Invalid config file");
        }
    }

    public List<User> getAllUsersFromTeam() {
        List<User> userList = null;
        try (SOAPConnection soapConnection = soapConnectionFactory.createConnection()) {

            SOAPMessage getSoapResponse = soapConnection.call(
                    SoapUtil.createSOAPRequest(getTeamSoapAction, spaceTeamUri, methodTeam, null),
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
                    SoapUtil.createSOAPRequest(addRouterSoapAction, spaceTeamUri, methodRouter, array),
                    routerSoapEndpointUrl);
            List<User> array1 = SoapUtil.parserToUserArray(addSoapMessage);
            System.out.println("Response SOAP Message:");
            addSoapMessage.writeTo(System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
