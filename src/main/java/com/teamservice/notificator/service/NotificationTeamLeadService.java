package com.teamservice.notificator.service;

import com.teamservice.notificator.util.PropertiesUtil;


public class NotificationTeamLeadService extends NotificatorService {
    public NotificationTeamLeadService() {
        getTeamSoapAction = PropertiesUtil.getProperty("team.action.teamlead.uri");
        addRouterSoapAction = PropertiesUtil.getProperty("router.action.teamlead.uri");
    }
}
