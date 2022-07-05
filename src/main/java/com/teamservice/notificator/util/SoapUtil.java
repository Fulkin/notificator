package com.teamservice.notificator.util;

import com.teamservice.notificator.model.ExpiredUsersArrayDAO;
import com.teamservice.notificator.model.UserDAO;
import com.teamservice.notificator.model.ExpiredUsersDAO;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.*;
import org.w3c.dom.Node;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

public class SoapUtil {

    public static List<UserDAO> parserToUserArray(SOAPMessage soapResponse) throws Exception {
        DOMSource source = new DOMSource(soapResponse.getSOAPBody().getChildNodes().item(0).getChildNodes().item(0));
        StringWriter stringResult = new StringWriter();
        TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
        String message = stringResult.toString();
        System.out.println(message);
        System.out.println();
        JAXBContext context = JAXBContext.newInstance(ExpiredUsersArrayDAO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ExpiredUsersDAO expiredUsersDAO = (ExpiredUsersDAO) unmarshaller.unmarshal(new StringReader(message));
        return expiredUsersDAO.getUserArray();
    }


    public static SOAPMessage createSOAPRequest(String uriSoapAction, List<UserDAO> array) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage, uriSoapAction, array);

        soapMessage.writeTo(System.out);
        System.out.println();
        soapMessage.saveChanges();
        return soapMessage;
    }

    public static SOAPMessage createByteSOAPRequest(Path path) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage message = messageFactory.createMessage();


        URL url = new URL("http://soap.router.aston.com/FileManager/uploadFile");
        String space = url.getProtocol() + "://" + url.getHost() + "/";
        String method = url.getPath().split("/")[2];


        String nameSpaceSoap = "soap";
        SOAPPart soapPart = message.getSOAPPart();
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(nameSpaceSoap, space);
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();

        SOAPElement soapElement = soapBody.addChildElement(method, nameSpaceSoap);
        String contextId = "494992293865";

        SOAPElement file1 = soapElement.addChildElement("file");


        DataHandler dataHandler = new DataHandler(new FileDataSource(path.toFile()));
        AttachmentPart attachment = message.createAttachmentPart(dataHandler);
        attachment.setContentId(contextId);
        message.addAttachmentPart(attachment);
        file1.addTextNode("cid:" + contextId);
        attachment.setContentType("application/pdf");
        message.saveChanges();
        message.writeTo(System.out);
        return message;
    }

    private static void createSoapEnvelope(SOAPMessage soapMessage, String urlString, List<UserDAO> array) throws Exception {
        URL url = new URL(urlString);
        String space = url.getProtocol() + "://" + url.getHost() + "/";
        String method = url.getPath().split("/")[2];

        String nameSpaceSoap = "end";
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
        soapBody.addChildElement(method, nameSpaceSoap);
        Node firstChild = soapBody.getFirstChild();

        JAXBContext context = JAXBContext.newInstance(ExpiredUsersDAO.class);
        Marshaller marshaller = context.createMarshaller();
        ExpiredUsersDAO expiredUsersDAO = new ExpiredUsersDAO();
        expiredUsersDAO.setUserArray(array);
        marshaller.marshal(expiredUsersDAO, firstChild);
    }
}
