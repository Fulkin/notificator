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


public class NotificationTeamLeadService {

    private static DateFormat dateToSoap = new SimpleDateFormat("yyyy-dd-MM'T'hh:mm:ss");
    private static String spaceUri;
    private static String teamSoapEndpointUrl;
    private static String routerSoapEndpointUrl;
    private static String getTeamSoapAction;
    private static String addRouterSoapAction;

    public NotificationTeamLeadService() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("properties\\connection.properties")) {
            Properties props = new Properties();
            props.load(is);
            routerSoapEndpointUrl = props.getProperty("router.url");
            teamSoapEndpointUrl = props.getProperty("team.url");
            spaceUri = props.getProperty("space.uri");
            getTeamSoapAction = spaceUri + props.getProperty("team.soapaction");
            addRouterSoapAction = spaceUri + props.getProperty("router.soapaction");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file");
        }
    }

    public List<User> getAllMemberAndSetToRouter() {
        List<User> userList = null;
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            SOAPMessage getSoapResponse = soapConnection.call(createGetSOAPRequest(getTeamSoapAction), teamSoapEndpointUrl);
            User[] array = parserToUserArray(getSoapResponse);
            SOAPMessage addSoapMessage = soapConnection.call(createAddSOAPRequest(addRouterSoapAction, array), routerSoapEndpointUrl);
            User[] array1 = parserToUserArray(addSoapMessage);
            System.out.println("Response SOAP Message:");
            addSoapMessage.writeTo(System.out);
            soapConnection.close();
            userList = Arrays.asList(array1.clone());
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
        }
        return userList;
    }

    private static SOAPMessage createGetSOAPRequest(String soapAction) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createGetSoapEnvelope(soapMessage);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();

        return soapMessage;
    }

    private static SOAPMessage createAddSOAPRequest(String soapAction, User[] array) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createAddSoapEnvelope(soapMessage, array);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();

        return soapMessage;
    }

    private static void createAddSoapEnvelope(SOAPMessage soapMessage, User[] array) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String myNamespace = "soap";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, spaceUri);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement actionSoap = soapBody.addChildElement("addUncheckedMembers", myNamespace);
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

    private static void createGetSoapEnvelope(SOAPMessage soapMessage) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String myNamespace = "soap";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, spaceUri);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        soapBody.addChildElement("getUncheckedMembers", myNamespace);
    }

    private User[] parserToUserArray(SOAPMessage soapResponse) throws Exception {
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
