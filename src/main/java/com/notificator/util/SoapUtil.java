package com.notificator.util;

import com.notificator.model.ExpiredUsersArrayDTO;
import com.notificator.model.ExpiredUsersDTO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.*;
import org.w3c.dom.Node;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

/**
 * Utility class for creation and parsing SOAP messages
 */
public class SoapUtil {

    private SoapUtil() {
    }

    /**
     * Get array expired users from SOAP messages
     * @param soapResponse - SOAP message containing expired users
     * @return - array expired users
     * @throws Exception - error parsing SOAP message
     */
    public static ExpiredUsersArrayDTO parserToUserArray(SOAPMessage soapResponse) throws Exception {
        Node item = soapResponse.getSOAPBody().getChildNodes().item(0).getChildNodes().item(0);
        DOMSource source = new DOMSource(item);
        StringWriter stringResult = new StringWriter();
        TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
        String message = stringResult.toString();
        JAXBContext context = JAXBContext.newInstance(ExpiredUsersArrayDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (ExpiredUsersArrayDTO) unmarshaller.unmarshal(new StringReader(message));
    }

    /**
     * Create SOAP message for sending as request
     * @param uriSoapAction - uri to which the response is sent
     * @param expiredUsers - group of users with their teamleader (or lector) to whom the response is sent
     * @return - response as SOAP message
     * @throws Exception - error creating SOAP message
     */
    public static SOAPMessage createSOAPRequest(String uriSoapAction, ExpiredUsersDTO expiredUsers) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        createSoapEnvelope(soapMessage, uriSoapAction, expiredUsers);
        soapMessage.saveChanges();
        return soapMessage;
    }

    /**
     * private method for creating body SOAP message
     * @param soapMessage - SOAP message that is sent as a request
     * @param urlString - uri to which the response is sent
     * @param expiredUsers - group of users with their teamleader (or lector) to whom the response is sent
     * @throws Exception - error creating SOAP message
     */
    private static void createSoapEnvelope(SOAPMessage soapMessage, String urlString, ExpiredUsersDTO expiredUsers) throws Exception {
        URL url = new URL(urlString);
        String space = url.getProtocol() + "://" + url.getHost() + "/";
        String method = url.getPath().split("/")[2];

        String nameSpaceSoap = "end";
        SOAPPart soapPart = soapMessage.getSOAPPart();

        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(nameSpaceSoap, space);

        SOAPBody soapBody = envelope.getBody();
        soapBody.addChildElement(method, nameSpaceSoap);
        if (expiredUsers == null) {
            return;
        }
        Node firstChild = soapBody.getFirstChild();
        JAXBContext context = JAXBContext.newInstance(ExpiredUsersArrayDTO.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(expiredUsers, firstChild);
    }
}
