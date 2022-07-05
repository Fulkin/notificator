package com.teamservice.notificator.service;

import com.teamservice.notificator.util.PropertiesUtil;


public class NotificationLectorService extends NotificatorService {
    public NotificationLectorService() {
        getTeamSoapAction = PropertiesUtil.getProperty("team.action.lector.uri");
        addRouterSoapAction = PropertiesUtil.getProperty("router.action.lector.uri");
    }
}
