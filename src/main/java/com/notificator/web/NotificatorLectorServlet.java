package com.notificator.web;

import com.notificator.model.ExpiredUsersArrayDTO;
import com.notificator.service.NotificatorService;
import com.notificator.util.PropertiesUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet to receive request from the team service for getting expired users and
 * send that users to the router service for lector
 */
public class NotificatorLectorServlet extends HttpServlet {

    private final NotificatorService notificatorService = new NotificatorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        ExpiredUsersArrayDTO expUsers = notificatorService.getAllUsersFromTeam(
                PropertiesUtil.getProperty("team.action.lector.uri")
        );
        if (expUsers == null) {
            return;
        }
        notificatorService.setUsersToRouter(
                expUsers, PropertiesUtil.getProperty("router.action.lector.uri")
        );
    }
}
