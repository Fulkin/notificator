package com.teamservice.notificator;

import com.teamservice.notificator.model.User;
import com.teamservice.notificator.service.NotificationLectorService;
import com.teamservice.notificator.service.NotificationTeamLeadService;
import com.teamservice.notificator.util.SoapUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class SOAPClientSAAJRouter {

    // SAAJ - SOAP Client Testing
    public static void main(String args[]) throws Exception {
        File file = new File("E:\\test\\soap-from-sender.txt");
        byte[] bytes = Files.readAllBytes(file.toPath());
        SoapUtil.createByteSOAPRequest(bytes);

        /*
        NotificationTeamLeadService notificationTeamLeadService = new NotificationTeamLeadService();
        List<User> allUsersFromTeam = notificationTeamLeadService.getAllUsersFromTeam();
        notificationTeamLeadService.setUsersToRouter(allUsersFromTeam);


        NotificationLectorService notificationLectorService = new NotificationLectorService();
        List<User> allUsersFromTeam1 = notificationLectorService.getAllUsersFromTeam();
        notificationLectorService.setUsersToRouter(allUsersFromTeam1);
        */
    }
}
