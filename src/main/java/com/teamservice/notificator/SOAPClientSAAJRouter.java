package com.teamservice.notificator;

import com.teamservice.notificator.model.User;
import com.teamservice.notificator.service.NotificationLectorService;
import com.teamservice.notificator.service.NotificationTeamLeadService;

import java.util.List;

public class SOAPClientSAAJRouter {
    // SAAJ - SOAP Client Testing
    public static void main(String args[]) throws Exception {
        NotificationTeamLeadService notificationTeamLeadService = new NotificationTeamLeadService();
        List<User> allUsersFromTeam = notificationTeamLeadService.getAllUsersFromTeam();
        notificationTeamLeadService.setUsersToRouter(allUsersFromTeam);

        NotificationLectorService notificationLectorService = new NotificationLectorService();
        List<User> allUsersFromTeam1 = notificationLectorService.getAllUsersFromTeam();
        notificationLectorService.setUsersToRouter(allUsersFromTeam1);
    }
}
