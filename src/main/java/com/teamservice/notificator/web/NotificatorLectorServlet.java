package com.teamservice.notificator.web;

import com.teamservice.notificator.model.ExpiredUsersDAO;
import com.teamservice.notificator.model.UserDAO;
import com.teamservice.notificator.service.NotificationTeamLeadService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class NotificatorLectorServlet extends HttpServlet {

    private NotificationTeamLeadService notificationLectorService = new NotificationTeamLeadService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        List<ExpiredUsersDAO> allMemberAndSetToRouter = notificationLectorService.getAllUsersFromTeam();
        notificationLectorService.setUsersToRouter(allMemberAndSetToRouter);
        allMemberAndSetToRouter.forEach(System.out::println);
    }
}
