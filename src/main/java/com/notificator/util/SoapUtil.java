package com.notificator.util;

import com.notificator.model.ExpiredUsersArray;
import com.notificator.model.ExpiredUsersDAO;
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

public class SoapUtil {

    public static ExpiredUsersArray parserToUserArray(SOAPMessage soapResponse) throws Exception {
        Node item = soapResponse.getSOAPBody().getChildNodes().item(0).getChildNodes().item(0);

        DOMSource source = new DOMSource(item);
        StringWriter stringResult = new StringWriter();
        TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
        String message = stringResult.toString();
        JAXBContext context = JAXBContext.newInstance(ExpiredUsersArray.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ExpiredUsersArray expiredUsersDAO = (ExpiredUsersArray) unmarshaller.unmarshal(new StringReader(message));
        return expiredUsersDAO;
    }

    public static SOAPMessage createSOAPRequest(String uriSoapAction, ExpiredUsersDAO expiredUsers) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage, uriSoapAction, expiredUsers);
        soapMessage.saveChanges();
        return soapMessage;
    }

    private static void createSoapEnvelope(SOAPMessage soapMessage, String urlString, ExpiredUsersDAO expiredUsers) throws Exception {
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

        soapBody.addChildElement(method, nameSpaceSoap);
        if (expiredUsers == null) {
            return;
        }
        Node firstChild = soapBody.getFirstChild();

        JAXBContext context = JAXBContext.newInstance(ExpiredUsersArray.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(expiredUsers, firstChild);
    }

    @Deprecated
    public static SOAPMessage createByteSOAPRequest(Path path) throws Exception {

        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage message = messageFactory.createMessage();
        MimeHeaders mimeHeaders = message.getMimeHeaders();


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
        String contextId = "1";

        DataHandler dataHandler = new DataHandler(new FileDataSource(path.toFile()));
        AttachmentPart attachment = message.createAttachmentPart(dataHandler);

        attachment.setContentId(contextId);
        message.addAttachmentPart(attachment);

        SOAPElement file1 = soapElement.addChildElement("file");
        file1.addTextNode("cid:" + contextId);
        message.saveChanges();
        return message;
    }
}
