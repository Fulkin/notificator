package com.teamservice.notificator.service;

import com.teamservice.notificator.model.User;
import com.teamservice.notificator.model.Users;
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


public class NotificationLectorService {

    private static DateFormat dateToSoap = new SimpleDateFormat("yyyy-dd-MM'T'hh:mm:ss");
    private static String nameSpace = "soap";
    private static String spaceTeamUri;
    private static String spaceRouterUri;
    private static String teamSoapEndpointUrl;
    private static String routerSoapEndpointUrl;
    private static String getTeamSoapAction;
    private static String addRouterSoapAction;
    User[] usertew = null;

    public NotificationLectorService() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("properties\\connection.properties")) {
            Properties props = new Properties();
            props.load(is);
            routerSoapEndpointUrl = props.getProperty("router.url");
            teamSoapEndpointUrl = props.getProperty("team.url");
            spaceTeamUri = props.getProperty("team.space.uri");
            spaceRouterUri = props.getProperty("router.space.uri");
            getTeamSoapAction = spaceTeamUri + props.getProperty("team.lector.soapaction");
            addRouterSoapAction = spaceTeamUri + props.getProperty("router.soapaction");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file");
        }
    }

    public List<User> getAllMemberAndSetToRouter() {
        List<User> userList = null;
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            SOAPMessage getSoapResponse = soapConnection.call(createSOAPRequest(getTeamSoapAction, null), teamSoapEndpointUrl);
            userList = parserToUserArray(getSoapResponse);
            SOAPMessage addSoapMessage = soapConnection.call(createSOAPRequest(addRouterSoapAction, userList), routerSoapEndpointUrl);
            userList = parserToUserArray(addSoapMessage);
            System.out.println("Response SOAP Message:");
            addSoapMessage.writeTo(System.out);
            soapConnection.close();
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
        }
        return userList;
    }

    private static SOAPMessage createSOAPRequest(String soapAction, List<User> array) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage, array);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();
        return soapMessage;
    }

    private static void createSoapEnvelope(SOAPMessage soapMessage, List<User> array) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();



        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(nameSpace, spaceRouterUri);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        if (array == null) {
            soapBody.addChildElement("getUncheckedMembers", nameSpace);
            return;
        }
        SOAPElement actionSoap = soapBody.addChildElement("addUncheckedMembers", nameSpace);
        SOAPElement membersSoap = actionSoap.addChildElement("users");


        for (User user : array) {
            SOAPElement itemSoap = membersSoap.addChildElement("item");
            SOAPElement soapBodyElem1 = itemSoap.addChildElement("telegramId");
            soapBodyElem1.addTextNode(String.valueOf(user.getTelegramId()));
            SOAPElement soapBodyElem2 = itemSoap.addChildElement("firstName");
            soapBodyElem2.addTextNode(user.getFirstName());
            SOAPElement soapBodyElem3 = itemSoap.addChildElement("lastName");
            soapBodyElem3.addTextNode(user.getLastName());
            SOAPElement soapBodyElem4 = itemSoap.addChildElement("lastTrack");
            String format = dateToSoap.format(user.getLastTrack());
            soapBodyElem4.addTextNode(format);
        }
    }

    private List<User> parserToUserArray(SOAPMessage soapResponse) throws Exception {
        DOMSource source = new DOMSource(soapResponse.getSOAPBody().getChildNodes().item(0).getChildNodes().item(0));
        StringWriter stringResult = new StringWriter();
        TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
        String message = stringResult.toString();
        System.out.println(message);
        JAXBContext context = JAXBContext.newInstance(Users.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Users users = (Users) unmarshaller.unmarshal(new StringReader(message));
        return users.getUserArray();
    }
}
