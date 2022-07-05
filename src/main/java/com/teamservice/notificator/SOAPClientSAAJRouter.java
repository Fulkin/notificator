package com.teamservice.notificator;

import com.teamservice.notificator.model.User;
import com.teamservice.notificator.service.NotificationLectorService;
import com.teamservice.notificator.service.NotificationTeamLeadService;
import com.teamservice.notificator.service.NotificatorService;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SOAPClientSAAJRouter {
    // SAAJ - SOAP Client Testing
    public static void main(String args[]) throws Exception {
//        NotificationTeamLeadService notificationTeamLeadService = new NotificationTeamLeadService();
//
//        Path path = Paths.get("E:\\test\\soap-from-sender.pdf");
//        File file = path.toFile();
//
//        notificationTeamLeadService.setFile(file.toPath());

//        List<User> users = new ArrayList<>();
//        users.add(new User(12241, "fwqq", "fqwfqw"));
//        users.add(new User(12241, "fwqq", "fqwfqw"));
//        users.add(new User(12241, "fwqq", "fqwfqw"));


        NotificatorService notificationLectorService = new NotificationLectorService();
        List<User> lector = notificationLectorService.getAllUsersFromTeam();
        lector.forEach(System.out::println);
        //notificationLectorService.setUsersToRouter(lector);

        NotificatorService notTeamLead = new NotificationTeamLeadService();
        List<User> teamLead = notTeamLead.getAllUsersFromTeam();
        teamLead.forEach(System.out::println);
        notificationLectorService.setUsersToRouter(teamLead);
    }
}
