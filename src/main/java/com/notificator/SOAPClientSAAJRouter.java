package com.notificator;

import com.notificator.model.ExpiredUsersArray;
import com.notificator.service.NotificatorService;
import com.notificator.util.PropertiesUtil;

public class SOAPClientSAAJRouter {
    // SAAJ - SOAP Client Testing
    public static void main(String args[]) throws Exception {
//        NotificatorService notificatorService = new NotificatorService();
//        Path path = Paths.get("E:\\test\\soap-from-sender.pdf");
//        File file = path.toFile();
//        notificatorService.setFile(path);

        NotificatorService notificationLectorService = new NotificatorService();
        ExpiredUsersArray lector = notificationLectorService.getAllUsersFromTeam(PropertiesUtil.getProperty("team.action.lector.uri"));
        notificationLectorService.setUsersToRouter(lector, PropertiesUtil.getProperty("router.action.lector.uri"));

        NotificatorService notTeamLead = new NotificatorService();
        ExpiredUsersArray teamLead = notTeamLead.getAllUsersFromTeam(PropertiesUtil.getProperty("team.action.teamlead.uri"));
        notTeamLead.setUsersToRouter(teamLead, PropertiesUtil.getProperty("router.action.teamlead.uri"));
    }
}
