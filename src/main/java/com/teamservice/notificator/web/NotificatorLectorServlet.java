package com.teamservice.notificator.web;

import com.teamservice.notificator.model.User;
import com.teamservice.notificator.service.NotificationLectorService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class NotificatorLectorServlet extends HttpServlet {

    private NotificationLectorService notificationLectorService = new NotificationLectorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        List<User> allMemberAndSetToRouter = notificationLectorService.getAllMemberAndSetToRouter();
        allMemberAndSetToRouter.forEach(System.out::println);
    }
}
