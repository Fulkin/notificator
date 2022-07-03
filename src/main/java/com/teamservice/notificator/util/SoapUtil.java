package com.teamservice.notificator.util;

import com.teamservice.notificator.model.User;
import com.teamservice.notificator.model.Users;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.*;
import org.w3c.dom.Node;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
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


    public static SOAPMessage createSOAPRequest(String uriSoapAction, List<User> array) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage, uriSoapAction, array);
        /*
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", uriSoapAction);
        */

        soapMessage.saveChanges();
        return soapMessage;
    }

    public static void createByteSOAPRequest(byte[] bytes) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage message = messageFactory.createMessage(new MimeHeaders(), new ByteArrayInputStream(bytes));
        //createSoapEnvelope(message, "http://soap.router.aston.com/RouterWebServiceSoap/getUncheckedMembersResponse", null);
        message.writeTo(System.out);
    }

    private static void createSoapEnvelope(SOAPMessage soapMessage, String urlString, List<User> array) throws Exception {
        URL url = new URL(urlString);
        String space = url.getProtocol() + "://" + url.getHost() + "/";
        String method = url.getPath().split("/")[2].replace("Response", "");

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
        Node firstChild = soapBody.getFirstChild();

        JAXBContext context = JAXBContext.newInstance(Users.class);
        Marshaller marshaller = context.createMarshaller();
        Users users = new Users();
        users.setUserArray(array);
        marshaller.marshal(users, firstChild);

    }
}