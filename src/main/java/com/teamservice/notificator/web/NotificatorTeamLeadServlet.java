package com.teamservice.notificator.web;

import com.teamservice.notificator.model.UserDAO;
import com.teamservice.notificator.service.NotificationTeamLeadService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class NotificatorTeamLeadServlet extends HttpServlet {

    private NotificationTeamLeadService notificationTeamLeadService = new NotificationTeamLeadService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        List<UserDAO> allMemberAndSetToRouter = notificationTeamLeadService.getAllUsersFromTeam();
        notificationTeamLeadService.setUsersToRouter(allMemberAndSetToRouter);
        allMemberAndSetToRouter.forEach(System.out::println);
    }
}
