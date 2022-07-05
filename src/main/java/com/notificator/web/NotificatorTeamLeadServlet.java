package com.notificator.web;

import com.notificator.model.ExpiredUsersArray;
import com.notificator.service.NotificatorService;
import com.notificator.util.PropertiesUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NotificatorTeamLeadServlet extends HttpServlet {

    private NotificatorService notificatorService = new NotificatorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        ExpiredUsersArray expUsers = notificatorService.getAllUsersFromTeam(
                PropertiesUtil.getProperty("team.action.teamlead.uri")
        );
        if (expUsers == null) {
            return;
        }
        notificatorService.setUsersToRouter(
                expUsers,
                PropertiesUtil.getProperty("router.action.teamlead.uri")
        );
    }
}
