package com.teamservice.notificator;

import com.teamservice.notificator.service.NotificationTeamLeadService;

public class SOAPClientSAAJRouter {

    // SAAJ - SOAP Client Testing
    public static void main(String args[]) {
        new NotificationTeamLeadService().getAllUsersFromTeam();
//        new NotificationLectorService().getAllMemberAndSetToRouter();
    }
}
