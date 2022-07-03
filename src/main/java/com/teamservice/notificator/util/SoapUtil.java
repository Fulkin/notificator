package com.teamservice.notificator.util;

import com.teamservice.notificator.model.User;
import com.teamservice.notificator.model.Users;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.*;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class SoapUtil {

    private static DateFormat dateToSoap = new SimpleDateFormat("yyyy-dd-MM'T'hh:mm:ss");


    public static List<User> parserToUserArray(SOAPMessage soapResponse) throws Exception {
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


    public static SOAPMessage createSOAPRequest(String uriSoapAction, String space, String method, List<User> array) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage, space, method, array);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", uriSoapAction);

        soapMessage.saveChanges();
        return soapMessage;
    }

    private static void createSoapEnvelope(SOAPMessage soapMessage, String space, String method, List<User> array) throws SOAPException {
        String nameSpaceSoap = "soap";
        SOAPPart soapPart = soapMessage.getSOAPPart();
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(nameSpaceSoap, space);
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        if (array == null) {
            soapBody.addChildElement(method, nameSpaceSoap);
            return;
        }
        SOAPElement actionSoap = soapBody.addChildElement(method, nameSpaceSoap);
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
}
